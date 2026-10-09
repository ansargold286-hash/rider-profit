package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MaintenanceExpense
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceExpenseDao {
    @Query("SELECT * FROM maintenance_expenses ORDER BY date DESC, createdAt DESC")
    fun getAllExpenses(): Flow<List<MaintenanceExpense>>

    @Query("SELECT * FROM maintenance_expenses WHERE date >= :fromTimestamp AND date <= :toTimestamp ORDER BY date DESC")
    fun getExpensesBetween(fromTimestamp: Long, toTimestamp: Long): Flow<List<MaintenanceExpense>>

    @Query("SELECT * FROM maintenance_expenses WHERE id = :id LIMIT 1")
    suspend fun getExpenseById(id: Long): MaintenanceExpense?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: MaintenanceExpense): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(expenses: List<MaintenanceExpense>)

    @Update
    suspend fun updateExpense(expense: MaintenanceExpense)

    @Delete
    suspend fun deleteExpense(expense: MaintenanceExpense)

    @Query("DELETE FROM maintenance_expenses WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM maintenance_expenses")
    suspend fun clearAll()
}
