package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.MaintenanceCategory
import com.example.data.model.MaintenanceExpense
import com.example.ui.theme.MaintenanceBlue

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddMaintenanceDialog(
    currency: String,
    existingItem: MaintenanceExpense? = null,
    onDismiss: () -> Unit,
    onSave: (MaintenanceExpense) -> Unit
) {
    var selectedCategory by remember {
        mutableStateOf(existingItem?.category ?: MaintenanceCategory.OIL_CHANGE.name)
    }
    var serviceTitle by remember {
        mutableStateOf(existingItem?.serviceTitle ?: "Engine Oil & Filter Change")
    }
    var amountText by remember {
        mutableStateOf(existingItem?.let { if (it.amount > 0) it.amount.toString() else "" } ?: "")
    }
    var spareParts by remember {
        mutableStateOf(existingItem?.spareParts ?: "")
    }
    var workshopName by remember {
        mutableStateOf(existingItem?.workshopName ?: "")
    }
    var odometerText by remember {
        mutableStateOf(existingItem?.let { if (it.odometerKm > 0) it.odometerKm.toString() else "" } ?: "")
    }
    var nextServiceText by remember {
        mutableStateOf(existingItem?.let { if (it.nextServiceOdometerKm > 0) it.nextServiceOdometerKm.toString() else "" } ?: "")
    }
    var notes by remember {
        mutableStateOf(existingItem?.notes ?: "")
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .testTag("add_maintenance_dialog"),
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
                            text = if (existingItem == null) "Log Bike Repair / Service" else "Edit Maintenance Record",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Record bike repair costs and spare parts",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Maintenance Category",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MaintenanceCategory.entries.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat.name,
                            onClick = {
                                selectedCategory = cat.name
                                if (serviceTitle.isBlank() || serviceTitle.startsWith("Engine Oil") || serviceTitle.startsWith("New") || serviceTitle.startsWith("Front")) {
                                    serviceTitle = cat.displayName
                                }
                            },
                            label = { Text(cat.displayName) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Service Title
                OutlinedTextField(
                    value = serviceTitle,
                    onValueChange = { serviceTitle = it },
                    label = { Text("Service Title / Fix Description") },
                    placeholder = { Text("e.g. Engine Oil Change + Filter") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("maintenance_title_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Cost / Amount
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Total Cost ($currency)") },
                    placeholder = { Text("e.g. 50.00") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("maintenance_amount_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Spare Parts
                OutlinedTextField(
                    value = spareParts,
                    onValueChange = { spareParts = it },
                    label = { Text("Spare Parts & Brands Installed") },
                    placeholder = { Text("e.g. Motul 7100 10W-40, OEM Filter, Spark plug") },
                    maxLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("maintenance_parts_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Workshop Name
                OutlinedTextField(
                    value = workshopName,
                    onValueChange = { workshopName = it },
                    label = { Text("Workshop / Mechanic Name") },
                    placeholder = { Text("e.g. Ah Huat Motor Workshop") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("maintenance_workshop_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Odometer & Next Service
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = odometerText,
                        onValueChange = { odometerText = it },
                        label = { Text("Current Odo (km)") },
                        placeholder = { Text("e.g. 16500") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("maintenance_odometer_input")
                    )

                    OutlinedTextField(
                        value = nextServiceText,
                        onValueChange = { nextServiceText = it },
                        label = { Text("Next Due (km)") },
                        placeholder = { Text("e.g. 19500") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("maintenance_next_due_input")
                    )
                }

                // Quick buttons for next service calculation
                val currentOdoNum = odometerText.toDoubleOrNull()
                if (currentOdoNum != null && currentOdoNum > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Quick Due:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedButton(
                            onClick = { nextServiceText = (currentOdoNum + 3000).toInt().toString() }
                        ) {
                            Text("+3,000 km (Oil)", style = MaterialTheme.typography.labelSmall)
                        }
                        OutlinedButton(
                            onClick = { nextServiceText = (currentOdoNum + 5000).toInt().toString() }
                        ) {
                            Text("+5,000 km", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Detailed Issue Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Issue Description & Notes") },
                    placeholder = { Text("e.g. Bike had sluggish acceleration, spark plug was dark. Runs smoothly now.") },
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("maintenance_notes_input")
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val expense = MaintenanceExpense(
                            id = existingItem?.id ?: 0,
                            date = existingItem?.date ?: System.currentTimeMillis(),
                            amount = amountText.toDoubleOrNull() ?: 0.0,
                            category = selectedCategory,
                            serviceTitle = serviceTitle.ifBlank { "Bike Maintenance" },
                            spareParts = spareParts,
                            workshopName = workshopName,
                            odometerKm = odometerText.toDoubleOrNull() ?: 0.0,
                            nextServiceOdometerKm = nextServiceText.toDoubleOrNull() ?: 0.0,
                            notes = notes
                        )
                        onSave(expense)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("save_maintenance_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaintenanceBlue)
                ) {
                    Text(
                        text = if (existingItem == null) "Save Maintenance Record" else "Update Record",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
