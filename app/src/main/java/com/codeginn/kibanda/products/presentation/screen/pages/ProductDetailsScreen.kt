@file:OptIn(ExperimentalMaterial3Api::class)

package com.codeginn.kibanda.products.presentation.screen.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.codeginn.kibanda.cartcheckout.presentation.viewmodel.CartViewModel
import com.codeginn.kibanda.products.domain.model.Product
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductDetailsScreen(
    product: Product,
    modifier: Modifier = Modifier,
    cartViewmodel : CartViewModel = koinViewModel<CartViewModel>(),
    onBack: () -> Unit = {}
){
    var quantity by rememberSaveable {
        mutableIntStateOf(1)
    }
    Scaffold(
        topBar = {
            ProductDetailsScreenTopBar(product, onBack)
        }
    ) {
        Column(
            modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = product.productImage,
                    contentDescription = product.productName,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = product.productName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start
                        ) {
                            IconButton(
                                onClick = {
                                    if (quantity > 1) cartViewmodel.updateQuantity(product.productID, quantity--)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.RemoveCircle,
                                    contentDescription = "Reduce Quantity"
                                )
                            }
                            Text(
                                text = quantity.toString(),
                                style = MaterialTheme.typography.titleSmall
                            )
                            IconButton(
                                onClick = {
                                    cartViewmodel.updateQuantity(product.productID, quantity++)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AddCircle,
                                    contentDescription = "Increase Quantity"
                                )
                            }
                        }
                        Text(
                            text = "KES ${product.productUnitPrice}",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                    FilledTonalButton(
                        onClick = {
                            cartViewmodel.addToCart(product.copy(
                                productQuantity = quantity
                            ))
                        }
                    ) {
                        Text(
                            text = "Add To Cart",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                }
            }

        }
    }

}

@Composable
fun ProductDetailsScreenTopBar(product: Product, onGoBack : () -> Unit = {}){
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onGoBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go Back"
                )
            }
        },
        title = {
            Text(
                text = "${product.productName} Screen",
                style = MaterialTheme.typography.titleMedium
            )
        }
    )
}