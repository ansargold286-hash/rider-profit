package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.MaintenanceExpense
import com.example.data.model.PetrolExpense
import com.example.data.model.WeeklyEarnings
import com.example.ui.components.AddEarningsDialog
import com.example.ui.components.AddMaintenanceDialog
import com.example.ui.components.AddPetrolDialog
import com.example.ui.components.RiderSettingsDialog
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EarningsScreen
import com.example.ui.screens.MaintenanceScreen
import com.example.ui.screens.PetrolScreen
import com.example.ui.theme.PandaPinkPrimary
import com.example.ui.theme.RiderFinanceTheme
import com.example.ui.viewmodel.RiderFinanceViewModel

enum class NavigationScreen(val title: String, val icon: ImageVector) {
    DASHBOARD("Dashboard", Icons.Default.Dashboard),
    EARNINGS("Earnings", Icons.Default.Payments),
    PETROL("Petrol", Icons.Default.LocalGasStation),
    MAINTENANCE("Repairs", Icons.Default.Build)
}

class MainActivity : ComponentActivity() {

    private val viewModel: RiderFinanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RiderFinanceTheme {
                RiderFinanceApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiderFinanceApp(
    viewModel: RiderFinanceViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var currentScreen by remember { mutableStateOf(NavigationScreen.DASHBOARD) }

    // Dialog state
    var showAddEarningsDialog by remember { mutableStateOf(false) }
    var editingEarnings by remember { mutableStateOf<WeeklyEarnings?>(null) }

    var showAddPetrolDialog by remember { mutableStateOf(false) }
    var editingPetrol by remember { mutableStateOf<PetrolExpense?>(null) }

    var showAddMaintenanceDialog by remember { mutableStateOf(false) }
    var editingMaintenance by remember { mutableStateOf<MaintenanceExpense?>(null) }

    var showSettingsDialog by remember { mutableStateOf(false) }

    // Handle back button when on sub-screens
    BackHandler(enabled = currentScreen != NavigationScreen.DASHBOARD) {
        currentScreen = NavigationScreen.DASHBOARD
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(PandaPinkPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsBike,
                                contentDescription = "Logo",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Rider Profit",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showSettingsDialog = true },
                        modifier = Modifier.testTag("settings_top_action_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.testTag("main_navigation_bar")
            ) {
                NavigationScreen.entries.forEach { screen ->
                    NavigationBarItem(
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        icon = {
                            Icon(imageVector = screen.icon, contentDescription = screen.title)
                        },
                        label = { Text(screen.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PandaPinkPrimary,
                            selectedTextColor = PandaPinkPrimary,
                            indicatorColor = PandaPinkPrimary.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_item_${screen.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                NavigationScreen.DASHBOARD -> {
                    DashboardScreen(
                        uiState = uiState,
                        onPeriodSelected = { viewModel.setPeriod(it) },
                        onQuickAddEarnings = {
                            editingEarnings = null
                            showAddEarningsDialog = true
                        },
                        onQuickAddPetrol = {
                            editingPetrol = null
                            showAddPetrolDialog = true
                        },
                        onQuickAddMaintenance = {
                            editingMaintenance = null
                            showAddMaintenanceDialog = true
                        }
                    )
                }

                NavigationScreen.EARNINGS -> {
                    EarningsScreen(
                        uiState = uiState,
                        onAddEarnings = {
                            editingEarnings = null
                            showAddEarningsDialog = true
                        },
                        onEditEarnings = { earning ->
                            editingEarnings = earning
                            showAddEarningsDialog = true
                        },
                        onDeleteEarnings = { id ->
                            viewModel.deleteWeeklyEarnings(id)
                        }
                    )
                }

                NavigationScreen.PETROL -> {
                    PetrolScreen(
                        uiState = uiState,
                        onAddPetrol = {
                            editingPetrol = null
                            showAddPetrolDialog = true
                        },
                        onEditPetrol = { petrol ->
                            editingPetrol = petrol
                            showAddPetrolDialog = true
                        },
                        onDeletePetrol = { id ->
                            viewModel.deletePetrolExpense(id)
                        }
                    )
                }

                NavigationScreen.MAINTENANCE -> {
                    MaintenanceScreen(
                        uiState = uiState,
                        onAddMaintenance = {
                            editingMaintenance = null
                            showAddMaintenanceDialog = true
                        },
                        onEditMaintenance = { maintenance ->
                            editingMaintenance = maintenance
                            showAddMaintenanceDialog = true
                        },
                        onDeleteMaintenance = { id ->
                            viewModel.deleteMaintenanceExpense(id)
                        }
                    )
                }
            }
        }
    }

    // Dialogs
    if (showAddEarningsDialog) {
        AddEarningsDialog(
            currency = uiState.currencySymbol,
            existingItem = editingEarnings,
            onDismiss = {
                showAddEarningsDialog = false
                editingEarnings = null
            },
            onSave = { earning ->
                if (editingEarnings == null) {
                    viewModel.addWeeklyEarnings(earning)
                } else {
                    viewModel.updateWeeklyEarnings(earning)
                }
                showAddEarningsDialog = false
                editingEarnings = null
            }
        )
    }

    if (showAddPetrolDialog) {
        AddPetrolDialog(
            currency = uiState.currencySymbol,
            existingItem = editingPetrol,
            onDismiss = {
                showAddPetrolDialog = false
                editingPetrol = null
            },
            onSave = { petrol ->
                if (editingPetrol == null) {
                    viewModel.addPetrolExpense(petrol)
                } else {
                    viewModel.updatePetrolExpense(petrol)
                }
                showAddPetrolDialog = false
                editingPetrol = null
            }
        )
    }

    if (showAddMaintenanceDialog) {
        AddMaintenanceDialog(
            currency = uiState.currencySymbol,
            existingItem = editingMaintenance,
            onDismiss = {
                showAddMaintenanceDialog = false
                editingMaintenance = null
            },
            onSave = { maintenance ->
                if (editingMaintenance == null) {
                    viewModel.addMaintenanceExpense(maintenance)
                } else {
                    viewModel.updateMaintenanceExpense(maintenance)
                }
                showAddMaintenanceDialog = false
                editingMaintenance = null
            }
        )
    }

    if (showSettingsDialog) {
        RiderSettingsDialog(
            currentCurrency = uiState.currencySymbol,
            currentBikeModel = uiState.bikeModel,
            currentBikePlate = uiState.bikePlate,
            onCurrencySelected = { viewModel.setCurrency(it) },
            onBikeDetailsUpdated = { model, plate -> viewModel.updateBikeDetails(model, plate) },
            onResetDemoData = { viewModel.resetToSampleData() },
            onClearAllData = { viewModel.clearAllData() },
            onDismiss = { showSettingsDialog = false }
        )
    }
}
