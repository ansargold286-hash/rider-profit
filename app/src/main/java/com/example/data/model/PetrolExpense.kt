package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "petrol_expenses")
data class PetrolExpense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Long,
    val amount: Double,
    val liters: Double = 0.0,
    val pricePerLiter: Double = if (liters > 0) amount / liters else 0.0,
    val odometerKm: Double = 0.0,
    val fuelStation: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
