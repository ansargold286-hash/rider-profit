package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.MaintenanceExpense
import com.example.data.model.PetrolExpense
import com.example.data.model.WeeklyEarnings

@Database(
    entities = [
        WeeklyEarnings::class,
        PetrolExpense::class,
        MaintenanceExpense::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RiderDatabase : RoomDatabase() {
    abstract fun weeklyEarningsDao(): WeeklyEarningsDao
    abstract fun petrolExpenseDao(): PetrolExpenseDao
    abstract fun maintenanceExpenseDao(): MaintenanceExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: RiderDatabase? = null

        fun getDatabase(context: Context): RiderDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RiderDatabase::class.java,
                    "rider_profit_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
