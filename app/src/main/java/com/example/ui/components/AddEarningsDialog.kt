package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.WeeklyEarnings
import java.util.Calendar

@Composable
fun AddEarningsDialog(
    currency: String,
    existingItem: WeeklyEarnings? = null,
    onDismiss: () -> Unit,
    onSave: (WeeklyEarnings) -> Unit
) {
    val cal = remember { Calendar.getInstance() }
    val currentWeekNum = remember { cal.get(Calendar.WEEK_OF_YEAR) }

    var weekLabel by remember {
        mutableStateOf(existingItem?.weekLabel ?: "Week $currentWeekNum")
    }
    var baseEarnings by remember {
        mutableStateOf(existingItem?.let { if (it.baseEarnings > 0) it.baseEarnings.toString() else "" } ?: "")
    }
    var questBonus by remember {
        mutableStateOf(existingItem?.let { if (it.questBonus > 0) it.questBonus.toString() else "" } ?: "")
    }
    var tips by remember {
        mutableStateOf(existingItem?.let { if (it.tips > 0) it.tips.toString() else "" } ?: "")
    }
    var ordersDelivered by remember {
        mutableStateOf(existingItem?.let { if (it.ordersDelivered > 0) it.ordersDelivered.toString() else "" } ?: "")
    }
    var hoursWorked by remember {
        mutableStateOf(existingItem?.let { if (it.hoursWorked > 0) it.hoursWorked.toString() else "" } ?: "")
    }
    var payoutStatus by remember {
        mutableStateOf(existingItem?.payoutStatus ?: "Paid")
    }
    var notes by remember {
        mutableStateOf(existingItem?.notes ?: "")
    }

    val baseVal = baseEarnings.toDoubleOrNull() ?: 0.0
    val questVal = questBonus.toDoubleOrNull() ?: 0.0
    val tipsVal = tips.toDoubleOrNull() ?: 0.0
    val totalCalculated = baseVal + questVal + tipsVal

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .testTag("add_earnings_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (existingItem == null) "Log Foodpanda Payout" else "Edit Weekly Payout",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Record earnings collected for the week",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = weekLabel,
                    onValueChange = { weekLabel = it },
                    label = { Text("Week Name / Label") },
                    placeholder = { Text("e.g. Week $currentWeekNum") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("earnings_week_label_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Base Earnings
                OutlinedTextField(
                    value = baseEarnings,
                    onValueChange = { baseEarnings = it },
                    label = { Text("Base Delivery Pay ($currency)") },
                    placeholder = { Text("e.g. 650.00") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("earnings_base_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = questBonus,
                        onValueChange = { questBonus = it },
                        label = { Text("Quest / Bonus ($currency)") },
                        placeholder = { Text("e.g. 120.00") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("earnings_quest_input")
                    )

                    OutlinedTextField(
                        value = tips,
                        onValueChange = { tips = it },
                        label = { Text("Tips ($currency)") },
                        placeholder = { Text("e.g. 35.00") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("earnings_tips_input")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Calculated Total Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Gross Payout:",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "$currency ${String.format(java.util.Locale.US, "%,.2f", totalCalculated)}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = ordersDelivered,
                        onValueChange = { ordersDelivered = it },
                        label = { Text("Orders Completed") },
                        placeholder = { Text("e.g. 95") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("earnings_orders_input")
                    )

                    OutlinedTextField(
                        value = hoursWorked,
                        onValueChange = { hoursWorked = it },
                        label = { Text("Road Hours") },
                        placeholder = { Text("e.g. 38.5") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("earnings_hours_input")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Payout Status",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Paid", "Processing", "Pending").forEach { status ->
                        FilterChip(
                            selected = payoutStatus == status,
                            onClick = { payoutStatus = status },
                            label = { Text(status) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes / Zone Details") },
                    placeholder = { Text("e.g. Heavy rain surge bonus on Friday night") },
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("earnings_notes_input")
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val start = existingItem?.startDate ?: System.currentTimeMillis() - 7 * 86400000L
                        val end = existingItem?.endDate ?: System.currentTimeMillis()
                        val record = WeeklyEarnings(
                            id = existingItem?.id ?: 0,
                            weekLabel = weekLabel.ifBlank { "Week $currentWeekNum" },
                            startDate = start,
                            endDate = end,
                            baseEarnings = baseVal,
                            questBonus = questVal,
                            tips = tipsVal,
                            totalEarnings = totalCalculated,
                            ordersDelivered = ordersDelivered.toIntOrNull() ?: 0,
                            hoursWorked = hoursWorked.toDoubleOrNull() ?: 0.0,
                            payoutStatus = payoutStatus,
                            notes = notes
                        )
                        onSave(record)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("save_earnings_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = if (existingItem == null) "Save Weekly Payout" else "Update Payout",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
