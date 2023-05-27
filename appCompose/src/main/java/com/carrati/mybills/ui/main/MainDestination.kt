package com.carrati.mybills.appCompose.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MainDestination(val title: String, val icon: ImageVector, val route: String) {
    object Home : MainDestination("Tela inicial", Icons.Default.Home, "home")
    object Transactions : MainDestination("Transações ", Icons.Default.List, "transactions")
}
