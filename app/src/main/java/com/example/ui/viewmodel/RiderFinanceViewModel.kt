package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.RiderDatabase
import com.example.data.model.MaintenanceCategory
import com.example.data.model.MaintenanceExpense
import com.example.data.model.PetrolExpense
import com.example.data.model.WeeklyEarnings
import com.example.data.repository.RiderFinanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class RiderFinanceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RiderFinanceRepository

    private val _selectedPeriod = MutableStateFlow(DateFilterPeriod.THIS_MONTH)
    private val _currencySymbol = MutableStateFlow("Rs")
    private val _bikeModel = MutableStateFlow("Yamaha Y15ZR")
    private val _bikePlate = MutableStateFlow("WVP 8291")

    init {
        val db = RiderDatabase.getDatabase(application)
        repository = RiderFinanceRepository(
            db.weeklyEarningsDao(),
            db.petrolExpenseDao(),
            db.maintenanceExpenseDao()
        )

        viewModelScope.launch {
            repository.checkAndSeedInitialDataIfNeeded()
        }
    }

    val uiState: StateFlow<RiderFinanceUiState> = combine(
        repository.allWeeklyEarnings,
        repository.allPetrolExpenses,
        repository.allMaintenanceExpenses,
        _selectedPeriod,
        _currencySymbol
    ) { earnings, petrol, maintenance, period, currency ->
        computeUiState(earnings, petrol, maintenance, period, currency, _bikeModel.value, _bikePlate.value)
    }.combine(_bikeModel) { state, bike ->
        state.copy(bikeModel = bike)
    }.combine(_bikePlate) { state, plate ->
        state.copy(bikePlate = plate)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RiderFinanceUiState(isLoading = true)
    )

    private fun computeUiState(
        allEarnings: List<WeeklyEarnings>,
        allPetrol: List<PetrolExpense>,
        allMaintenance: List<MaintenanceExpense>,
        period: DateFilterPeriod,
        currency: String,
        bikeModel: String,
        bikePlate: String
    ): RiderFinanceUiState {
        val (startTime, endTime) = getTimeBoundsForPeriod(period)

        val filteredEarnings = when (period) {
            DateFilterPeriod.ALL_TIME -> allEarnings
            else -> allEarnings.filter { it.startDate >= startTime && it.endDate <= endTime + 86400000L }
        }

        val filteredPetrol = when (period) {
            DateFilterPeriod.ALL_TIME -> allPetrol
            else -> allPetrol.filter { it.date in startTime..endTime }
        }

        val filteredMaintenance = when (period) {
            DateFilterPeriod.ALL_TIME -> allMaintenance
            else -> allMaintenance.filter { it.date in startTime..endTime }
        }

        val grossIncome = filteredEarnings.sumOf { it.totalEarnings }
        val petrolCost = filteredPetrol.sumOf { it.amount }
        val maintenanceCost = filteredMaintenance.sumOf { it.amount }
        val totalExpenses = petrolCost + maintenanceCost
        val netProfit = grossIncome - totalExpenses
        val profitMargin = if (grossIncome > 0) (netProfit / grossIncome) * 100.0 else 0.0

        val totalOrders = filteredEarnings.sumOf { it.ordersDelivered }
        val profitPerOrder = if (totalOrders > 0) netProfit / totalOrders else 0.0

        val totalLiters = filteredPetrol.sumOf { it.liters }
        val avgCostPerLiter = if (totalLiters > 0) petrolCost / totalLiters else 0.0

        val latestOdo = maxOf(
            allPetrol.maxOfOrNull { it.odometerKm } ?: 0.0,
            allMaintenance.maxOfOrNull { it.odometerKm } ?: 0.0
        )

        val latestMaintenanceWithNext = allMaintenance
            .filter { it.nextServiceOdometerKm > 0 }
            .maxByOrNull { it.date }

        val categoryBreakdown = computeCategoryBreakdown(petrolCost, filteredMaintenance, totalExpenses)
        val weeklyComparison = computeWeeklyComparison(allEarnings, allPetrol, allMaintenance)

        return RiderFinanceUiState(
            earningsList = allEarnings,
            petrolList = allPetrol,
            maintenanceList = allMaintenance,
            selectedPeriod = period,
            currencySymbol = currency,
            bikeModel = bikeModel,
            bikePlate = bikePlate,
            grossIncome = grossIncome,
            petrolCost = petrolCost,
            maintenanceCost = maintenanceCost,
            totalExpenses = totalExpenses,
            netProfit = netProfit,
            profitMarginPercent = profitMargin,
            totalOrders = totalOrders,
            netProfitPerOrder = profitPerOrder,
            totalPetrolLiters = totalLiters,
            averageCostPerLiter = avgCostPerLiter,
            latestOdometer = latestOdo,
            nextMaintenanceDueKm = latestMaintenanceWithNext?.nextServiceOdometerKm,
            nextMaintenanceTitle = latestMaintenanceWithNext?.serviceTitle ?: "",
            categoryBreakdown = categoryBreakdown,
            weeklyComparison = weeklyComparison,
            isLoading = false
        )
    }

    private fun computeCategoryBreakdown(
        petrolCost: Double,
        maintenanceList: List<MaintenanceExpense>,
        totalExpenses: Double
    ): List<ExpenseCategoryItem> {
        if (totalExpenses <= 0.0) return emptyList()

        val list = mutableListOf<ExpenseCategoryItem>()

        // Petrol
        if (petrolCost > 0) {
            val pct = ((petrolCost / totalExpenses) * 100).toFloat()
            list.add(ExpenseCategoryItem("Petrol / Fuel", petrolCost, pct, 0xFFE65100))
        }

        // Maintenance by category
        val grouped = maintenanceList.groupBy { it.category }
        val colors = listOf(0xFF0D47A1, 0xFF7B1FA2, 0xFF00897B, 0xFFC2185B, 0xFF5D4037, 0xFF455A64)
        var colorIdx = 0

        grouped.forEach { (catName, items) ->
            val catTotal = items.sumOf { it.amount }
            if (catTotal > 0) {
                val pct = ((catTotal / totalExpenses) * 100).toFloat()
                val friendlyName = try {
                    MaintenanceCategory.valueOf(catName).displayName
                } catch (e: Exception) {
                    catName
                }
                list.add(
                    ExpenseCategoryItem(
                        categoryName = friendlyName,
                        amount = catTotal,
                        percentage = pct,
                        colorHex = colors[colorIdx % colors.size]
                    )
                )
                colorIdx++
            }
        }

        return list.sortedByDescending { it.amount }
    }

    private fun computeWeeklyComparison(
        allEarnings: List<WeeklyEarnings>,
        allPetrol: List<PetrolExpense>,
        allMaintenance: List<MaintenanceExpense>
    ): List<WeeklyComparisonItem> {
        val result = mutableListOf<WeeklyComparisonItem>()

        allEarnings.take(5).forEach { earning ->
            val weekPetrol = allPetrol.filter { it.date in earning.startDate..earning.endDate }
                .sumOf { it.amount }
            val weekMaint = allMaintenance.filter { it.date in earning.startDate..earning.endDate }
                .sumOf { it.amount }
            val weekExpense = weekPetrol + weekMaint
            val weekProfit = earning.totalEarnings - weekExpense

            val shortLabel = earning.weekLabel.split("(").firstOrNull()?.trim() ?: earning.weekLabel
            result.add(
                WeeklyComparisonItem(
                    label = shortLabel,
                    earnings = earning.totalEarnings,
                    expenses = weekExpense,
                    netProfit = weekProfit
                )
            )
        }

        return result
    }

    private fun getTimeBoundsForPeriod(period: DateFilterPeriod): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        val now = cal.timeInMillis

        return when (period) {
            DateFilterPeriod.THIS_WEEK -> {
                cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                val start = cal.timeInMillis
                cal.add(Calendar.DAY_OF_WEEK, 6)
                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59)
                Pair(start, cal.timeInMillis)
            }
            DateFilterPeriod.THIS_MONTH -> {
                cal.set(Calendar.DAY_OF_MONTH, 1)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                val start = cal.timeInMillis
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59)
                Pair(start, cal.timeInMillis)
            }
            DateFilterPeriod.LAST_MONTH -> {
                cal.add(Calendar.MONTH, -1)
                cal.set(Calendar.DAY_OF_MONTH, 1)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                val start = cal.timeInMillis
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59)
                Pair(start, cal.timeInMillis)
            }
            DateFilterPeriod.ALL_TIME -> {
                Pair(0L, now + 86400000L * 30)
            }
        }
    }

    // Filter & Setting Actions
    fun setPeriod(period: DateFilterPeriod) {
        _selectedPeriod.value = period
    }

    fun setCurrency(currency: String) {
        _currencySymbol.value = currency
    }

    fun updateBikeDetails(model: String, plate: String) {
        _bikeModel.value = model
        _bikePlate.value = plate
    }

    // Earnings
    fun addWeeklyEarnings(earnings: WeeklyEarnings) {
        viewModelScope.launch {
            repository.insertWeeklyEarnings(earnings)
        }
    }

    fun updateWeeklyEarnings(earnings: WeeklyEarnings) {
        viewModelScope.launch {
            repository.updateWeeklyEarnings(earnings)
        }
    }

    fun deleteWeeklyEarnings(id: Long) {
        viewModelScope.launch {
            repository.deleteWeeklyEarnings(id)
        }
    }

    // Petrol
    fun addPetrolExpense(expense: PetrolExpense) {
        viewModelScope.launch {
            repository.insertPetrolExpense(expense)
        }
    }

    fun updatePetrolExpense(expense: PetrolExpense) {
        viewModelScope.launch {
            repository.updatePetrolExpense(expense)
        }
    }

    fun deletePetrolExpense(id: Long) {
        viewModelScope.launch {
            repository.deletePetrolExpense(id)
        }
    }

    // Maintenance
    fun addMaintenanceExpense(expense: MaintenanceExpense) {
        viewModelScope.launch {
            repository.insertMaintenanceExpense(expense)
        }
    }

    fun updateMaintenanceExpense(expense: MaintenanceExpense) {
        viewModelScope.launch {
            repository.updateMaintenanceExpense(expense)
        }
    }

    fun deleteMaintenanceExpense(id: Long) {
        viewModelScope.launch {
            repository.deleteMaintenanceExpense(id)
        }
    }

    // Database Utilities
    fun resetToSampleData() {
        viewModelScope.launch {
            repository.resetWithSampleData()
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }
}
