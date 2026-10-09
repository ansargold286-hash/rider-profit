package com.example.ui.viewmodel

import com.example.data.model.MaintenanceExpense
import com.example.data.model.PetrolExpense
import com.example.data.model.WeeklyEarnings

enum class DateFilterPeriod(val title: String) {
    THIS_WEEK("This Week"),
    THIS_MONTH("This Month"),
    LAST_MONTH("Last Month"),
    ALL_TIME("All Time")
}

data class WeeklyComparisonItem(
    val label: String,
    val earnings: Double,
    val expenses: Double,
    val netProfit: Double
)

data class ExpenseCategoryItem(
    val categoryName: String,
    val amount: Double,
    val percentage: Float,
    val colorHex: Long
)

data class RiderFinanceUiState(
    val earningsList: List<WeeklyEarnings> = emptyList(),
    val petrolList: List<PetrolExpense> = emptyList(),
    val maintenanceList: List<MaintenanceExpense> = emptyList(),
    val selectedPeriod: DateFilterPeriod = DateFilterPeriod.THIS_MONTH,
    val currencySymbol: String = "Rs", // Rs, RM, ₱, ৳, S$, $, €
    val bikeModel: String = "Yamaha Y15ZR",
    val bikePlate: String = "WVP 8291",

    // Calculated for current filter period
    val grossIncome: Double = 0.0,
    val petrolCost: Double = 0.0,
    val maintenanceCost: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val netProfit: Double = 0.0,
    val profitMarginPercent: Double = 0.0,
    val totalOrders: Int = 0,
    val netProfitPerOrder: Double = 0.0,
    val totalPetrolLiters: Double = 0.0,
    val averageCostPerLiter: Double = 0.0,
    val latestOdometer: Double = 0.0,
    val nextMaintenanceDueKm: Double? = null,
    val nextMaintenanceTitle: String = "",

    val categoryBreakdown: List<ExpenseCategoryItem> = emptyList(),
    val weeklyComparison: List<WeeklyComparisonItem> = emptyList(),
    val isLoading: Boolean = false
)
