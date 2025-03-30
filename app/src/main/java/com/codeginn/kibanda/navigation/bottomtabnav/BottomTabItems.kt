package com.codeginn.kibanda.navigation.bottomtabnav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import com.codeginn.kibanda.R

val bottomTabItems = listOf<BottomTabItem>(
    BottomTabItem(
        tabLabel = R.string.home,
        tabDestination = BottomDestination.Home.name,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    BottomTabItem(
        tabLabel = R.string.cart,
        tabDestination = BottomDestination.Cart.name,
        selectedIcon = Icons.Filled.ShoppingCart,
        unselectedIcon = Icons.Outlined.ShoppingCart
    ),
    BottomTabItem(
        tabLabel = R.string.orders,
        tabDestination = BottomDestination.Orders.name,
        selectedIcon = Icons.AutoMirrored.Filled.List,
        unselectedIcon = Icons.AutoMirrored.Outlined.List
    ),
    BottomTabItem(
        tabLabel = R.string.account,
        tabDestination = BottomDestination.Account.name,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )
)