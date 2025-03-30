package com.codeginn.kibanda.products.domain.repository

import com.codeginn.kibanda.products.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun searchProducts(query: String): Flow<List<Product>>
    fun getAllFruits(): Flow<List<Product>>
    fun getAllVegetables(): Flow<List<Product>>
}