package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ExpenseBreakdownCard
import com.example.ui.components.HeroNetProfitCard
import com.example.ui.components.MiniMetricCard
import com.example.ui.components.WeeklyProfitChartCard
import com.example.ui.theme.MaintenanceBlue
import com.example.ui.theme.PandaPinkPrimary
import com.example.ui.theme.PetrolAmber
import com.example.ui.theme.ProfitGreen
import com.example.ui.viewmodel.DateFilterPeriod
import com.example.ui.viewmodel.RiderFinanceUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    uiState: RiderFinanceUiState,
    onPeriodSelected: (DateFilterPeriod) -> Unit,
    onQuickAddEarnings: () -> Unit,
    onQuickAddPetrol: () -> Unit,
    onQuickAddMaintenance: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currency = uiState.currencySymbol
    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Vehicle & Rider Badge
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("rider_badge_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(PandaPinkPrimary.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsBike,
                                contentDescription = "Bike",
                                tint = PandaPinkPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Foodpanda Delivery Partner",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "${uiState.bikeModel} • ${uiState.bikePlate}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (uiState.latestOdometer > 0) {
                        Text(
                            text = "${uiState.latestOdometer.toInt()} km",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Period Filter Chips
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Period",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Financial Period",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(DateFilterPeriod.entries.toTypedArray()) { period ->
                        FilterChip(
                            selected = uiState.selectedPeriod == period,
                            onClick = { onPeriodSelected(period) },
                            label = { Text(period.title) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PandaPinkPrimary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("period_chip_${period.name}")
                        )
                    }
                }
            }
        }

        // Hero Net Profit Card
        item {
            HeroNetProfitCard(
                currency = currency,
                grossIncome = uiState.grossIncome,
                totalExpenses = uiState.totalExpenses,
                netProfit = uiState.netProfit,
                profitMarginPercent = uiState.profitMarginPercent,
                ordersCount = uiState.totalOrders,
                netProfitPerOrder = uiState.netProfitPerOrder
            )
        }

        // Quick Action Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ElevatedButton(
                    onClick = onQuickAddEarnings,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quick_add_earnings_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = PandaPinkPrimary
                    ),
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "+ Payout", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }

                ElevatedButton(
                    onClick = onQuickAddPetrol,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quick_add_petrol_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = PetrolAmber
                    ),
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.LocalGasStation, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "+ Petrol", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }

                ElevatedButton(
                    onClick = onQuickAddMaintenance,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quick_add_maintenance_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaintenanceBlue
                    ),
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Build, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "+ Repair", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }

        // 2x2 Mini Metrics Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MiniMetricCard(
                    title = "Petrol Expense",
                    value = "$currency ${String.format(Locale.US, "%,.2f", uiState.petrolCost)}",
                    subtitle = "${String.format(Locale.US, "%.1f", uiState.totalPetrolLiters)} Liters pumped",
                    icon = Icons.Default.LocalGasStation,
                    accentColor = PetrolAmber,
                    modifier = Modifier.weight(1f)
                )

                MiniMetricCard(
                    title = "Bike Repairs",
                    value = "$currency ${String.format(Locale.US, "%,.2f", uiState.maintenanceCost)}",
                    subtitle = "${uiState.maintenanceList.size} service logs",
                    icon = Icons.Default.Build,
                    accentColor = MaintenanceBlue,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MiniMetricCard(
                    title = "Total Orders",
                    value = "${uiState.totalOrders}",
                    subtitle = "Delivered on shift",
                    icon = Icons.Default.DeliveryDining,
                    accentColor = PandaPinkPrimary,
                    modifier = Modifier.weight(1f)
                )

                val costPerOrder = if (uiState.totalOrders > 0) uiState.totalExpenses / uiState.totalOrders else 0.0
                MiniMetricCard(
                    title = "Expense/Order",
                    value = "$currency ${String.format(Locale.US, "%.2f", costPerOrder)}",
                    subtitle = "Vehicle cost per delivery",
                    icon = Icons.Default.Warning,
                    accentColor = if (costPerOrder > 2.0) Color(0xFFDE350B) else ProfitGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Active Maintenance Alert (if upcoming service)
        if (uiState.nextMaintenanceDueKm != null && uiState.latestOdometer > 0) {
            item {
                val remainingKm = (uiState.nextMaintenanceDueKm - uiState.latestOdometer).toInt()
                val isUrgent = remainingKm <= 500

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("maintenance_alert_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isUrgent) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    color = if (isUrgent) Color(0xFFD32F2F).copy(alpha = 0.15f)
                                    else ProfitGreen.copy(alpha = 0.15f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isUrgent) Icons.Default.Warning else Icons.Default.NotificationsActive,
                                contentDescription = "Alert",
                                tint = if (isUrgent) Color(0xFFD32F2F) else ProfitGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isUrgent) "Service Due Soon!" else "Next Service Reminder",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isUrgent) Color(0xFFC62828) else Color(0xFF1B5E20)
                            )
                            Text(
                                text = "Next ${uiState.nextMaintenanceTitle.ifBlank { "Service" }} due at ${uiState.nextMaintenanceDueKm.toInt()} km (in ~$remainingKm km)",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }

        // Visual Charts
        item {
            ExpenseBreakdownCard(
                categories = uiState.categoryBreakdown,
                totalExpenses = uiState.totalExpenses,
                currency = currency
            )
        }

        item {
            WeeklyProfitChartCard(
                weeklyData = uiState.weeklyComparison,
                currency = currency
            )
        }

        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
