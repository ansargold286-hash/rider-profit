package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.PetrolExpense
import kotlinx.coroutines.flow.Flow

@Dao
interface PetrolExpenseDao {
    @Query("SELECT * FROM petrol_expenses ORDER BY date DESC, createdAt DESC")
    fun getAllExpenses(): Flow<List<PetrolExpense>>

    @Query("SELECT * FROM petrol_expenses WHERE date >= :fromTimestamp AND date <= :toTimestamp ORDER BY date DESC")
    fun getExpensesBetween(fromTimestamp: Long, toTimestamp: Long): Flow<List<PetrolExpense>>

    @Query("SELECT * FROM petrol_expenses WHERE id = :id LIMIT 1")
    suspend fun getExpenseById(id: Long): PetrolExpense?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: PetrolExpense): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(expenses: List<PetrolExpense>)

    @Update
    suspend fun updateExpense(expense: PetrolExpense)

    @Delete
    suspend fun deleteExpense(expense: PetrolExpense)

    @Query("DELETE FROM petrol_expenses WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM petrol_expenses")
    suspend fun clearAll()
}
