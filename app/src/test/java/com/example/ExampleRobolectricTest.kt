package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.MaintenanceExpense
import com.example.data.model.PetrolExpense
import com.example.data.model.WeeklyEarnings
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Rider Profit", appName)
    }

    @Test
    fun `calculate net profit accurately`() {
        val earning = WeeklyEarnings(
            id = 1,
            weekLabel = "Week 41",
            startDate = 1000L,
            endDate = 2000L,
            baseEarnings = 600.0,
            questBonus = 100.0,
            tips = 50.0,
            totalEarnings = 750.0,
            ordersDelivered = 80
        )

        val petrol = PetrolExpense(
            id = 1,
            date = 1500L,
            amount = 45.0,
            liters = 20.0
        )

        val maintenance = MaintenanceExpense(
            id = 1,
            date = 1600L,
            amount = 55.0,
            category = "OIL_CHANGE",
            serviceTitle = "Engine Oil"
        )

        val totalExpenses = petrol.amount + maintenance.amount
        val netProfit = earning.totalEarnings - totalExpenses
        val profitMargin = (netProfit / earning.totalEarnings) * 100.0

        assertEquals(100.0, totalExpenses, 0.001)
        assertEquals(650.0, netProfit, 0.001)
        assertEquals(86.666, profitMargin, 0.01)
    }
}
