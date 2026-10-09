package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maintenance_expenses")
data class MaintenanceExpense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Long,
    val amount: Double,
    val category: String,
    val serviceTitle: String,
    val spareParts: String = "",
    val workshopName: String = "",
    val odometerKm: Double = 0.0,
    val nextServiceOdometerKm: Double = 0.0,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
