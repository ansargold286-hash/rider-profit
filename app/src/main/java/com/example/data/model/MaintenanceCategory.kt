package com.example.data.model

enum class MaintenanceCategory(val displayName: String, val iconName: String) {
    OIL_CHANGE("Engine Oil Change", "Oil"),
    TIRE_PUNCTURE_OR_REPLACEMENT("Tire Repair / New Tire", "Tire"),
    BRAKE_PADS("Brake Pads & Calipers", "Brakes"),
    CHAIN_AND_SPROCKET("Drive Chain & Sprocket", "Chain"),
    ENGINE_TUNING("Engine Tuning & Valve", "Engine"),
    BATTERY_ELECTRICAL("Battery & Spark Plug", "Battery"),
    GENERAL_SERVICING("Full Periodic Servicing", "Service"),
    OTHER("Other Parts & Accessories", "Other")
}
