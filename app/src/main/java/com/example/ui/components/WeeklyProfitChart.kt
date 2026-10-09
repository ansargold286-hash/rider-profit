package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PandaPinkPrimary
import com.example.ui.theme.PetrolAmber
import com.example.ui.theme.ProfitGreen
import com.example.ui.viewmodel.WeeklyComparisonItem
import java.util.Locale

@Composable
fun WeeklyProfitChartCard(
    weeklyData: List<WeeklyComparisonItem>,
    currency: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("weekly_profit_chart_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = "Weekly Chart",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Weekly Earnings vs Expenses",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Legends
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChartLegend(color = PandaPinkPrimary, label = "Gross Earnings")
                ChartLegend(color = PetrolAmber, label = "Expenses")
                ChartLegend(color = ProfitGreen, label = "Net Profit")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (weeklyData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No weekly payouts recorded yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                val maxVal = (weeklyData.maxOfOrNull { maxOf(it.earnings, it.expenses, it.netProfit) } ?: 100.0)
                    .coerceAtLeast(100.0)

                // Custom Canvas Bar Chart
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    val width = size.width
                    val height = size.height - 24.dp.toPx()
                    val groupCount = weeklyData.size
                    val groupWidth = width / groupCount
                    val barWidth = (groupWidth * 0.22f).coerceAtMost(22.dp.toPx())

                    // Baseline grid line
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.4f),
                        start = Offset(0f, height),
                        end = Offset(width, height),
                        strokeWidth = 2f
                    )

                    weeklyData.forEachIndexed { i, item ->
                        val groupCenterX = (i * groupWidth) + (groupWidth / 2)

                        // 3 bars: Earnings, Expenses, Net Profit
                        val earningsHeight = (item.earnings.toFloat() / maxVal.toFloat() * height).coerceAtLeast(4f)
                        val expensesHeight = (item.expenses.toFloat() / maxVal.toFloat() * height).coerceAtLeast(4f)
                        val profitHeight = (item.netProfit.coerceAtLeast(0.0).toFloat() / maxVal.toFloat() * height).coerceAtLeast(4f)

                        val bar1X = groupCenterX - barWidth * 1.6f
                        val bar2X = groupCenterX - barWidth * 0.5f
                        val bar3X = groupCenterX + barWidth * 0.6f

                        // Bar 1: Earnings (Panda Pink)
                        drawRoundRect(
                            color = PandaPinkPrimary,
                            topLeft = Offset(bar1X, height - earningsHeight),
                            size = Size(barWidth, earningsHeight),
                            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )

                        // Bar 2: Expenses (Amber)
                        drawRoundRect(
                            color = PetrolAmber,
                            topLeft = Offset(bar2X, height - expensesHeight),
                            size = Size(barWidth, expensesHeight),
                            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )

                        // Bar 3: Net Profit (Green)
                        drawRoundRect(
                            color = ProfitGreen,
                            topLeft = Offset(bar3X, height - profitHeight),
                            size = Size(barWidth, profitHeight),
                            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )
                    }
                }

                // X-Axis Labels & Summary Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    weeklyData.forEach { item ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(70.dp)
                        ) {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                            Text(
                                text = "+$currency${item.netProfit.toInt()}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = ProfitGreen,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChartLegend(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
