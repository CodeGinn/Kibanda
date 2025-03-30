@file:OptIn(ExperimentalMaterial3Api::class)

package com.codeginn.kibanda.cartcheckout.presentation.presentation.tab


import android.R.attr.onClick
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.codeginn.kibanda.cartcheckout.domain.model.CartItem
import com.codeginn.kibanda.cartcheckout.presentation.viewmodel.CartViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartTab(
    cartViewModel: CartViewModel = koinViewModel<CartViewModel>()
){
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalAmount by cartViewModel.totalAmount.collectAsState()
    val deliveryFee by cartViewModel.deliveryFee.collectAsState()
    val subtotal by cartViewModel.subtotal.collectAsState()
    var cartItemQuantity by rememberSaveable {
        mutableIntStateOf(1)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Cart",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .wrapContentSize()
            ) {
                SummaryRow(
                    title = "Subtotal : ",
                    value = "KES $subtotal"
                )
                SummaryRow(
                    title = "Delivery Fee : ",
                    value = "KES $deliveryFee"
                )
                HorizontalDivider()
                SummaryRow(
                    title = "Total : ",
                    value = "KES $totalAmount"
                )
                Button(onClick = { cartViewModel.clearCart() }) {
                    Text("Clear Cart")
                }
                Button(
                    onClick = { /* Handle checkout */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Proceed to Checkout")
                }
            }

        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            if (cartItems.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp,vertical = 8.dp)
                ) {
                    items(cartItems) {item ->
                        CartItemRow(
                            item = item,
                            onQuantityChange = {itemId, newQuantity ->
                                cartViewModel.updateQuantity(itemId, newQuantity)
                            },
                            onRemove = {itemId ->
                                cartViewModel.removeFromCart(itemId)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Log.d("CartItemsList", item.itemId)
                    }
                }

            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Your cart is empty.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            }
        }
    }



}

@Composable
fun CartItemRow(
    item: CartItem,
    onQuantityChange: (String, Int) -> Unit,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var cartQuantity by rememberSaveable {
        mutableIntStateOf(1)
    }
    Row (
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(120.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
        ) {
            AsyncImage(
                model = item.itemImage,
                contentDescription = item.itemName,
                contentScale = ContentScale.Fit
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = item.itemName,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
            ) {
                IconButton(
                    onClick = {
                        if (item.itemQuantity > 1) onQuantityChange(item.itemId, cartQuantity - 1)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease Quantity"
                    )
                }
                Text("Qty: $cartQuantity")
                IconButton(
                    onClick = {
                        onQuantityChange(item.itemId,  cartQuantity + 1)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase Quantity"
                    )
                }
            }
            IconButton(
                onClick = {
                    onRemove(item.itemId)
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Item",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }

    }
}

@Composable
fun SummaryRow(title: String, value: String){
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }

}