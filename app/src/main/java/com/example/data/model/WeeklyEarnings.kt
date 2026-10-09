package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_earnings")
data class WeeklyEarnings(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val weekLabel: String,
    val startDate: Long,
    val endDate: Long,
    val baseEarnings: Double,
    val questBonus: Double = 0.0,
    val tips: Double = 0.0,
    val totalEarnings: Double = baseEarnings + questBonus + tips,
    val ordersDelivered: Int = 0,
    val hoursWorked: Double = 0.0,
    val payoutStatus: String = "Paid",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
