package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.WeeklyEarnings
import kotlinx.coroutines.flow.Flow

@Dao
interface WeeklyEarningsDao {
    @Query("SELECT * FROM weekly_earnings ORDER BY startDate DESC, createdAt DESC")
    fun getAllEarnings(): Flow<List<WeeklyEarnings>>

    @Query("SELECT * FROM weekly_earnings WHERE startDate >= :fromTimestamp AND endDate <= :toTimestamp ORDER BY startDate DESC")
    fun getEarningsBetween(fromTimestamp: Long, toTimestamp: Long): Flow<List<WeeklyEarnings>>

    @Query("SELECT * FROM weekly_earnings WHERE id = :id LIMIT 1")
    suspend fun getEarningsById(id: Long): WeeklyEarnings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEarnings(earnings: WeeklyEarnings): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(earningsList: List<WeeklyEarnings>)

    @Update
    suspend fun updateEarnings(earnings: WeeklyEarnings)

    @Delete
    suspend fun deleteEarnings(earnings: WeeklyEarnings)

    @Query("DELETE FROM weekly_earnings WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM weekly_earnings")
    suspend fun clearAll()
}
