package com.codeginn.kibanda.products.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.codeginn.kibanda.products.domain.repository.ProductRepository

class ProductViewModel(
    private val productRepository: ProductRepository
): ViewModel() {
    val fruitsResult = productRepository.getAllFruits()
    val vegetablesResult = productRepository.getAllVegetables()
}