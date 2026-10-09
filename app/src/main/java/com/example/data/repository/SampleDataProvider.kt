package com.example.data.repository

import com.example.data.model.MaintenanceCategory
import com.example.data.model.MaintenanceExpense
import com.example.data.model.PetrolExpense
import com.example.data.model.WeeklyEarnings
import java.util.Calendar

object SampleDataProvider {

    fun generateInitialEarnings(): List<WeeklyEarnings> {
        val cal = Calendar.getInstance()

        // Week 1 (Current Week)
        val end1 = cal.timeInMillis
        cal.add(Calendar.DAY_OF_YEAR, -6)
        val start1 = cal.timeInMillis

        // Week 2 (Last Week)
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val end2 = cal.timeInMillis
        cal.add(Calendar.DAY_OF_YEAR, -6)
        val start2 = cal.timeInMillis

        // Week 3 (2 weeks ago)
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val end3 = cal.timeInMillis
        cal.add(Calendar.DAY_OF_YEAR, -6)
        val start3 = cal.timeInMillis

        // Week 4 (3 weeks ago)
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val end4 = cal.timeInMillis
        cal.add(Calendar.DAY_OF_YEAR, -6)
        val start4 = cal.timeInMillis

        return listOf(
            WeeklyEarnings(
                id = 1,
                weekLabel = "Week 41 (Current)",
                startDate = start1,
                endDate = end1,
                baseEarnings = 640.0,
                questBonus = 120.0,
                tips = 45.0,
                totalEarnings = 805.0,
                ordersDelivered = 96,
                hoursWorked = 37.5,
                payoutStatus = "Processing",
                notes = "Weekend surge quest completed (40 orders milestone reached)."
            ),
            WeeklyEarnings(
                id = 2,
                weekLabel = "Week 40",
                startDate = start2,
                endDate = end2,
                baseEarnings = 710.0,
                questBonus = 150.0,
                tips = 58.0,
                totalEarnings = 918.0,
                ordersDelivered = 108,
                hoursWorked = 41.0,
                payoutStatus = "Paid",
                notes = "Rain bonus incentives applied on Friday & Sunday shifts."
            ),
            WeeklyEarnings(
                id = 3,
                weekLabel = "Week 39",
                startDate = start3,
                endDate = end3,
                baseEarnings = 590.0,
                questBonus = 90.0,
                tips = 34.0,
                totalEarnings = 714.0,
                ordersDelivered = 84,
                hoursWorked = 34.0,
                payoutStatus = "Paid",
                notes = "Standard weekday shifts in Subang / Petaling zone."
            ),
            WeeklyEarnings(
                id = 4,
                weekLabel = "Week 38",
                startDate = start4,
                endDate = end4,
                baseEarnings = 675.0,
                questBonus = 135.0,
                tips = 42.0,
                totalEarnings = 852.0,
                ordersDelivered = 98,
                hoursWorked = 39.0,
                payoutStatus = "Paid",
                notes = "Lunch & Dinner peak rush quests unlocked."
            )
        )
    }

    fun generateInitialPetrol(): List<PetrolExpense> {
        val cal = Calendar.getInstance()
        val expenses = mutableListOf<PetrolExpense>()

        val daysAgo = listOf(1, 3, 5, 8, 11, 14, 17, 20, 23, 26)
        val stations = listOf("Petronas", "Shell", "Caltex", "BHPetrol", "Petronas")
        var currentOdo = 16850.0

        daysAgo.forEachIndexed { index, days ->
            cal.timeInMillis = System.currentTimeMillis()
            cal.add(Calendar.DAY_OF_YEAR, -days)
            val liters = 3.6 + (index % 3) * 0.4
            val pricePerLiter = 2.05
            val amount = (liters * pricePerLiter * 100).toInt() / 100.0

            expenses.add(
                PetrolExpense(
                    id = (index + 1).toLong(),
                    date = cal.timeInMillis,
                    amount = amount,
                    liters = (liters * 10).toInt() / 10.0,
                    pricePerLiter = pricePerLiter,
                    odometerKm = currentOdo - (days * 42.0),
                    fuelStation = stations[index % stations.size],
                    notes = if (index % 2 == 0) "Full tank before evening dinner shift" else "Mid-day top up during rush hour"
                )
            )
        }

        return expenses
    }

    fun generateInitialMaintenance(): List<MaintenanceExpense> {
        val cal = Calendar.getInstance()

        // 4 days ago - Engine Oil Change
        cal.timeInMillis = System.currentTimeMillis()
        cal.add(Calendar.DAY_OF_YEAR, -4)
        val m1 = MaintenanceExpense(
            id = 1,
            date = cal.timeInMillis,
            amount = 45.0,
            category = MaintenanceCategory.OIL_CHANGE.name,
            serviceTitle = "Engine Oil & Oil Filter Replacement",
            spareParts = "Motul 7100 10W-40 4T Full Synthetic, OEM Filter",
            workshopName = "Speedy Moto Service Subang",
            odometerKm = 16700.0,
            nextServiceOdometerKm = 19700.0,
            notes = "Smooth engine feel, checked engine coolant & chain slack."
        )

        // 12 days ago - Brake Pads
        cal.timeInMillis = System.currentTimeMillis()
        cal.add(Calendar.DAY_OF_YEAR, -12)
        val m2 = MaintenanceExpense(
            id = 2,
            date = cal.timeInMillis,
            amount = 35.0,
            category = MaintenanceCategory.BRAKE_PADS.name,
            serviceTitle = "Front & Rear Brake Pads Replacement",
            spareParts = "Nissin Ceramic Brake Pads (Front + Rear)",
            workshopName = "Ah Huat Motor Workshop",
            odometerKm = 16350.0,
            nextServiceOdometerKm = 24000.0,
            notes = "Front pads were worn down to 15%. Braking bite restored."
        )

        // 22 days ago - Tire Puncture repair
        cal.timeInMillis = System.currentTimeMillis()
        cal.add(Calendar.DAY_OF_YEAR, -22)
        val m3 = MaintenanceExpense(
            id = 3,
            date = cal.timeInMillis,
            amount = 12.0,
            category = MaintenanceCategory.TIRE_PUNCTURE_OR_REPLACEMENT.name,
            serviceTitle = "Tubeless Tire Puncture Patch",
            spareParts = "Heavy-duty rubber mushroom plug patch",
            workshopName = "Petronas Gas Station Mechanic",
            odometerKm = 15900.0,
            nextServiceOdometerKm = 0.0,
            notes = "Nail found on rear tire during delivery in industrial area."
        )

        // 35 days ago - Chain lube & sprocket adjust
        cal.timeInMillis = System.currentTimeMillis()
        cal.add(Calendar.DAY_OF_YEAR, -35)
        val m4 = MaintenanceExpense(
            id = 4,
            date = cal.timeInMillis,
            amount = 25.0,
            category = MaintenanceCategory.CHAIN_AND_SPROCKET.name,
            serviceTitle = "Drive Chain Tension & Degrease Lube",
            spareParts = "DID O-Ring Chain cleaner & Ipone synthetic lube",
            workshopName = "Speedy Moto Service Subang",
            odometerKm = 15300.0,
            nextServiceOdometerKm = 18000.0,
            notes = "Tightened loose slack and lubricated links."
        )

        return listOf(m1, m2, m3, m4)
    }
}
