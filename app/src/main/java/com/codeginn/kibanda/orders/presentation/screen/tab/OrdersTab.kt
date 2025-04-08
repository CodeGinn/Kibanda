@file:OptIn(ExperimentalMaterial3Api::class)

package com.codeginn.kibanda.orders.presentation.screen.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codeginn.kibanda.orders.domain.model.Order
import com.codeginn.kibanda.orders.presentation.viewmodel.OrderViewModel
import com.codeginn.kibanda.utils.Resource
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrdersTab(
    modifier: Modifier = Modifier,
    orderViewModel: OrderViewModel = koinViewModel<OrderViewModel>()
){
    val ordersResource by orderViewModel.userOrders.collectAsState()
    val placeOrderResult by orderViewModel.placeOrderResult.collectAsState()
    val updateStatusResult by orderViewModel.updateStatusResult.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Orders",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            when (ordersResource) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is Resource.Success -> {
                    val orders = ordersResource.data
                    if (orders!!.isEmpty()) {
                        Text("No orders placed yet.")
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(orders) { order ->
                                OrderItemCard(order = order, onStatusUpdate = orderViewModel::updateOrderDeliveryStatus)
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Text(
                        text = "Error fetching orders: ${ordersResource.errorMessage}",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is Resource.Idle<*> -> TODO()
            }
            placeOrderResult?.let { result ->
                when (result) {
                    is Resource.Success -> {
                        LaunchedEffect(result.data) { // Use LaunchedEffect for one-time actions
                            SnackbarHostState().showSnackbar("Order placed successfully! Order ID: ${result.data}")
                            orderViewModel.clearPlaceOrderResult()
                        }
                    }
                    is Resource.Error -> {
                        LaunchedEffect(result.errorMessage) {
                            SnackbarHostState().showSnackbar("Failed to place order: ${result.errorMessage}")
                            orderViewModel.clearPlaceOrderResult()
                        }
                    }
                    is Resource.Loading -> {
                        // Optionally show a loading indicator for placing order
                        Text("Placing order...")
                    }
                    is Resource.Idle<*> -> TODO()
                }
            }
            updateStatusResult?.let{result ->
                when(result){
                    is Resource.Error<*> -> {
                        LaunchedEffect(result.errorMessage) {
                            SnackbarHostState().showSnackbar("Failed to update delivery status: ${result.errorMessage}")
                            orderViewModel.clearUpdateStatusResult()
                        }
                    }
                    is Resource.Idle<*> -> TODO()
                    is Resource.Loading<*> -> {
                        Text("Updating status...")
                    }
                    is Resource.Success<*> -> {
                        LaunchedEffect(Unit) {
                            SnackbarHostState().showSnackbar("Delivery status updated successfully!")
                            orderViewModel.clearUpdateStatusResult()
                            orderViewModel.fetchUserOrders() // Refresh the order list
                        }
                    }
                }
                SnackbarHost(hostState = remember { SnackbarHostState() })
            }
        }
    }

}

@Composable
fun OrderItemCard(order: Order, onStatusUpdate: (String, String) -> Unit) {
    Card (elevation = CardDefaults.elevatedCardElevation(2.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Order ID: ${order.orderId}")
            Text(text = "Order Date: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(order.orderDate))}")
            Text(text = "Total Amount: KES ${order.totalAmount}")
            Text(text = "Delivery Status: ${order.deliveryStatus}")
            Spacer(modifier = Modifier.height(8.dp))
            DeliveryStatusDropdown(currentStatus = order.deliveryStatus) { newStatus ->
                onStatusUpdate(order.orderId, newStatus)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Items:")
            order.items.forEach { item ->
                Text(text = "- ${item.itemName} (Qty: ${item.itemQuantity}, Price: KES ${item.itemPrice}, Total: KES ${item.totalItemPrice})")
            }
        }
    }
}

@Composable
fun DeliveryStatusDropdown(currentStatus: String, onStatusSelected: (String) -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val statuses = listOf("Pending", "Processing", "Shipped", "Delivered", "Cancelled")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            readOnly = true,
            value = currentStatus,
            onValueChange = {},
            label = { Text("Delivery Status") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            statuses.forEach { status ->
                DropdownMenuItem(
                    onClick = {
                        onStatusSelected(status)
                        expanded = false
                    },
                    text = {
                        Text(text = status)
                    }
                )
            }
        }
    }
}