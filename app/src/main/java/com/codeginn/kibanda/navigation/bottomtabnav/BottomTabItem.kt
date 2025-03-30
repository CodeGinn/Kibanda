package com.codeginn.kibanda.navigation.bottomtabnav

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomTabItem(
    @StringRes val tabLabel: Int,
    val tabDestination: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)
