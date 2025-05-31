package com.example.testarief

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings

sealed class MainScreen(
    val route: String,
    val label: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    data object Home : MainScreen(
        AppRoutes.HOME,
        R.string.bottom_nav_home,
        Icons.Default.Home
    )
    data object Requests : MainScreen(
        AppRoutes.REQUESTS,
        R.string.bottom_nav_requests,
        Icons.Default.List
    )
    data object Settings : MainScreen(
        AppRoutes.SETTINGS,
        R.string.bottom_nav_settings,
        Icons.Default.Settings
    )
}
