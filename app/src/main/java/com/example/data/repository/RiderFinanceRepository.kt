package com.example.data.repository

import com.example.data.local.MaintenanceExpenseDao
import com.example.data.local.PetrolExpenseDao
import com.example.data.local.WeeklyEarningsDao
import com.example.data.model.MaintenanceExpense
import com.example.data.model.PetrolExpense
import com.example.data.model.WeeklyEarnings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class RiderFinanceRepository(
    private val weeklyEarningsDao: WeeklyEarningsDao,
    private val petrolExpenseDao: PetrolExpenseDao,
    private val maintenanceExpenseDao: MaintenanceExpenseDao
) {
    val allWeeklyEarnings: Flow<List<WeeklyEarnings>> = weeklyEarningsDao.getAllEarnings()
    val allPetrolExpenses: Flow<List<PetrolExpense>> = petrolExpenseDao.getAllExpenses()
    val allMaintenanceExpenses: Flow<List<MaintenanceExpense>> = maintenanceExpenseDao.getAllExpenses()

    suspend fun checkAndSeedInitialDataIfNeeded() {
        val existingEarnings = weeklyEarningsDao.getAllEarnings().first()
        val existingPetrol = petrolExpenseDao.getAllExpenses().first()
        val existingMaintenance = maintenanceExpenseDao.getAllExpenses().first()

        if (existingEarnings.isEmpty() && existingPetrol.isEmpty() && existingMaintenance.isEmpty()) {
            weeklyEarningsDao.insertAll(SampleDataProvider.generateInitialEarnings())
            petrolExpenseDao.insertAll(SampleDataProvider.generateInitialPetrol())
            maintenanceExpenseDao.insertAll(SampleDataProvider.generateInitialMaintenance())
        }
    }

    suspend fun resetWithSampleData() {
        weeklyEarningsDao.clearAll()
        petrolExpenseDao.clearAll()
        maintenanceExpenseDao.clearAll()

        weeklyEarningsDao.insertAll(SampleDataProvider.generateInitialEarnings())
        petrolExpenseDao.insertAll(SampleDataProvider.generateInitialPetrol())
        maintenanceExpenseDao.insertAll(SampleDataProvider.generateInitialMaintenance())
    }

    suspend fun clearAllData() {
        weeklyEarningsDao.clearAll()
        petrolExpenseDao.clearAll()
        maintenanceExpenseDao.clearAll()
    }

    // Earnings
    suspend fun insertWeeklyEarnings(earnings: WeeklyEarnings): Long =
        weeklyEarningsDao.insertEarnings(earnings)

    suspend fun updateWeeklyEarnings(earnings: WeeklyEarnings) =
        weeklyEarningsDao.updateEarnings(earnings)

    suspend fun deleteWeeklyEarnings(id: Long) =
        weeklyEarningsDao.deleteById(id)

    // Petrol
    suspend fun insertPetrolExpense(expense: PetrolExpense): Long =
        petrolExpenseDao.insertExpense(expense)

    suspend fun updatePetrolExpense(expense: PetrolExpense) =
        petrolExpenseDao.updateExpense(expense)

    suspend fun deletePetrolExpense(id: Long) =
        petrolExpenseDao.deleteById(id)

    // Maintenance
    suspend fun insertMaintenanceExpense(expense: MaintenanceExpense): Long =
        maintenanceExpenseDao.insertExpense(expense)

    suspend fun updateMaintenanceExpense(expense: MaintenanceExpense) =
        maintenanceExpenseDao.updateExpense(expense)

    suspend fun deleteMaintenanceExpense(id: Long) =
        maintenanceExpenseDao.deleteById(id)
}
