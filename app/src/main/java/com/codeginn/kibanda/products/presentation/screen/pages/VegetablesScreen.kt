package com.codeginn.kibanda.products.presentation.screen.pages

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codeginn.kibanda.products.domain.model.Product
import com.codeginn.kibanda.products.presentation.viewmodel.ProductViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun VegetablesScreen(
    productViewModel: ProductViewModel = koinViewModel<ProductViewModel>(),
    onProductCardClicked: (Product) -> Unit = {}
){
    val vegetables by productViewModel.vegetablesResult.collectAsState(emptyList())

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(vegetables) { vegetable ->
            ProductCard(vegetable,onCardClicked = onProductCardClicked)
        }
    }

}
