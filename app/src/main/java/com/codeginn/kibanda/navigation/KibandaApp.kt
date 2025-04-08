package com.codeginn.kibanda.navigation


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.codeginn.kibanda.account.presentation.screen.tab.AccountTab
import com.codeginn.kibanda.authentication.presentation.screen.LoginScreen
import com.codeginn.kibanda.authentication.presentation.screen.SignupScreen
import com.codeginn.kibanda.cartcheckout.presentation.screen.tab.CartTab
import com.codeginn.kibanda.mpesa.presentation.screen.MpesaScreen
import com.codeginn.kibanda.navigation.authnav.Login
import com.codeginn.kibanda.navigation.authnav.Signup
import com.codeginn.kibanda.navigation.bottomtabnav.BottomDestination
import com.codeginn.kibanda.navigation.bottomtabnav.bottomTabItems
import com.codeginn.kibanda.navigation.mainscreennav.CustomNavType
import com.codeginn.kibanda.navigation.mainscreennav.MpesaScreen
import com.codeginn.kibanda.navigation.mainscreennav.ProductDetails
import com.codeginn.kibanda.navigation.mainscreennav.SearchScreen
import com.codeginn.kibanda.navigation.postroutes.PostsPage
import com.codeginn.kibanda.orders.presentation.screen.tab.OrdersTab
import com.codeginn.kibanda.posts.presentation.screen.PostsScreen
import com.codeginn.kibanda.products.domain.model.Product
import com.codeginn.kibanda.products.presentation.screen.pages.ProductDetailsScreen
import com.codeginn.kibanda.products.presentation.screen.pages.SearchResultsScreen
import com.codeginn.kibanda.products.presentation.screen.tab.HomeTab
import kotlin.reflect.typeOf

@Composable
fun KibandaApp(
    modifier: Modifier = Modifier,
    kibandaNavController: NavHostController = rememberNavController()
){

    val listOfBottomTabNavigation = listOf(
        BottomDestination.Home.name,
        BottomDestination.Cart.name,
        BottomDestination.Orders.name,
        BottomDestination.Account.name
    )

    Scaffold(
        modifier.fillMaxSize(),
        bottomBar = {
            if (kibandaNavController.currentBackStackEntryAsState().value?.destination?.route in listOfBottomTabNavigation){
                BottomTabBar(bottomNavController = kibandaNavController)
            }
        }
    ) {
        NavHost(
            kibandaNavController,
            startDestination = Login,
            modifier = Modifier.padding(it)
        ) {
            composable<Signup> {
                SignupScreen(
                    onRegister = {
                        kibandaNavController.navigate(Login)
                    },
                    onLoginClicked = {
                        kibandaNavController.navigate(Login)
                    }
                )
            }
            composable<Login> {
                LoginScreen(
                    onLogin = {
                        kibandaNavController.navigate(BottomDestination.Home.name)
                    },
                    onRegister = {
                        kibandaNavController.navigate(Signup)
                    }
                )
            }
            composable<ProductDetails>(
                typeMap = mapOf(
                    typeOf<Product>() to CustomNavType.ProductType
                )
            ) {
                val args = it.toRoute<ProductDetails>()
                ProductDetailsScreen(
                    product = args.product,
                    onBack = {
                    kibandaNavController.popBackStack()
                })
            }

            composable(BottomDestination.Home.name) {
                HomeTab(
                    onProductCardClicked = {product ->
                        kibandaNavController.navigate(ProductDetails(product))
                    },
                    onSearch = {searchProduct ->
                        kibandaNavController.navigate(SearchScreen(searchProduct))
                    }
                )
            }
            composable(BottomDestination.Cart.name) {
                CartTab(
                    onCheckOut = {
                        kibandaNavController.navigate(MpesaScreen){
                            popUpTo(BottomDestination.Cart.name){
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(BottomDestination.Orders.name) {
                OrdersTab()
            }
            composable(BottomDestination.Account.name) {
                AccountTab(
                    backToLogin = {
                        kibandaNavController.navigate(Login)
                    }
                )
            }
            composable<SearchScreen>(
                typeMap = mapOf(
                    typeOf<Product>() to CustomNavType.ProductType
                )
            ) {
                val searchArgs = it.toRoute<SearchScreen>()
                SearchResultsScreen(
                    product = searchArgs.searchProduct
                )
            }
            composable<PostsPage>{
                PostsScreen()
            }
            composable<MpesaScreen>{
                MpesaScreen(
                    navigateToOrderScreen = {
                        kibandaNavController.navigate(BottomDestination.Orders.name)
                    }
                )
            }
        }

    }

}

@Composable
fun BottomTabBar(bottomNavController: NavHostController){
    var selectedTab by rememberSaveable {
        mutableIntStateOf(0)
    }


    NavigationBar {
        bottomTabItems.forEachIndexed { index, bottomItem ->
            NavigationBarItem(
                selected = index == selectedTab,
                onClick = {
                    selectedTab = index
                    bottomNavController.navigate(bottomItem.tabDestination){
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = when(selectedTab == index){
                        true -> bottomItem.selectedIcon
                        false -> bottomItem.unselectedIcon
                    },
                    contentDescription = null
                )},
                label = {
                    Text(
                        text = stringResource(bottomItem.tabLabel),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }
}