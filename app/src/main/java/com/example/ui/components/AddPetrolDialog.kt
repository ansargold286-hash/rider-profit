package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.example.data.model.PetrolExpense
import com.example.ui.theme.PetrolAmber
import java.util.Locale

@Composable
fun AddPetrolDialog(
    currency: String,
    existingItem: PetrolExpense? = null,
    onDismiss: () -> Unit,
    onSave: (PetrolExpense) -> Unit
) {
    var amountText by remember {
        mutableStateOf(existingItem?.let { if (it.amount > 0) it.amount.toString() else "" } ?: "")
    }
    var litersText by remember {
        mutableStateOf(existingItem?.let { if (it.liters > 0) it.liters.toString() else "" } ?: "")
    }
    var odometerText by remember {
        mutableStateOf(existingItem?.let { if (it.odometerKm > 0) it.odometerKm.toString() else "" } ?: "")
    }
    var fuelStation by remember {
        mutableStateOf(existingItem?.fuelStation ?: "Petronas")
    }
    var notes by remember {
        mutableStateOf(existingItem?.notes ?: "")
    }

    val amountVal = amountText.toDoubleOrNull() ?: 0.0
    val litersVal = litersText.toDoubleOrNull() ?: 0.0
    val pricePerLiter = if (litersVal > 0) amountVal / litersVal else 0.0

    val stationOptions = listOf("Petronas", "Shell", "Caltex", "BHPetrol", "TotalEnergies", "Other")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .testTag("add_petrol_dialog"),
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
                            text = if (existingItem == null) "Log Petrol Fill-up" else "Edit Petrol Log",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Daily fuel expense and quantity",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Fuel Cost
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Total Fuel Cost ($currency)") },
                    placeholder = { Text("e.g. 8.50") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("petrol_amount_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Liters
                OutlinedTextField(
                    value = litersText,
                    onValueChange = { litersText = it },
                    label = { Text("Fuel Quantity (Liters)") },
                    placeholder = { Text("e.g. 4.15") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("petrol_liters_input")
                )

                if (pricePerLiter > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Calculated Price: $currency ${String.format(Locale.US, "%.2f", pricePerLiter)} / Liter",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = PetrolAmber
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Odometer
                OutlinedTextField(
                    value = odometerText,
                    onValueChange = { odometerText = it },
                    label = { Text("Bike Odometer Reading (km) - Optional") },
                    placeholder = { Text("e.g. 16850") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("petrol_odometer_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Fuel Station / Brand",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    stationOptions.take(3).forEach { st ->
                        FilterChip(
                            selected = fuelStation == st,
                            onClick = { fuelStation = st },
                            label = { Text(st) }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    stationOptions.drop(3).forEach { st ->
                        FilterChip(
                            selected = fuelStation == st,
                            onClick = { fuelStation = st },
                            label = { Text(st) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes / Shift Details") },
                    placeholder = { Text("e.g. Full tank for weekend night shift") },
                    maxLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("petrol_notes_input")
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val expense = PetrolExpense(
                            id = existingItem?.id ?: 0,
                            date = existingItem?.date ?: System.currentTimeMillis(),
                            amount = amountVal,
                            liters = litersVal,
                            pricePerLiter = pricePerLiter,
                            odometerKm = odometerText.toDoubleOrNull() ?: 0.0,
                            fuelStation = fuelStation,
                            notes = notes
                        )
                        onSave(expense)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("save_petrol_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PetrolAmber)
                ) {
                    Text(
                        text = if (existingItem == null) "Log Petrol Expense" else "Update Petrol Log",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
