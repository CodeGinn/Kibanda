package com.codeginn.kibanda.products.presentation.screen.pages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.codeginn.kibanda.cartcheckout.presentation.viewmodel.CartViewModel
import com.codeginn.kibanda.products.domain.model.Product
import com.codeginn.kibanda.products.presentation.viewmodel.ProductViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FruitsScreen(
    productViewModel: ProductViewModel = koinViewModel<ProductViewModel>(),
    onProductCardClicked: (Product) -> Unit = {}
){
    val fruits by productViewModel.fruitsResult.collectAsState(emptyList())


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (fruits.isEmpty()){
            CircularProgressIndicator(modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                items(fruits) { fruit ->
                    ProductCard(fruit,onCardClicked = onProductCardClicked)
                }
            }
        }
    }




}

@Composable
fun ProductCard(
    product: Product,
    cartViewmodel : CartViewModel = koinViewModel<CartViewModel>(),
    onCardClicked: (Product) -> Unit = {},
){

    ElevatedCard(
        modifier = Modifier
            .height(280.dp)
            .width(200.dp)
            .padding(end = 12.dp, bottom = 12.dp),
        onClick = {
            onCardClicked(product)
        },
        elevation = CardDefaults.elevatedCardElevation(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentAlignment = Alignment.Center
        ){
            Log.d("ImageLoading", "Loading Image URL: ${product.productImage}")
            AsyncImage(
                model = product.productImage,
                contentDescription = product.productName,
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "PID: ${product.productID}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text =product.productName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text =product.productUnit,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "KES ${product.productUnitPrice}",
                    style = MaterialTheme.typography.titleSmall
                )
                IconButton(
                    onClick = {
                        cartViewmodel.addToCart(product)
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddShoppingCart,
                        contentDescription = "Add To Cart"
                    )
                }
            }
        }


    }

}
