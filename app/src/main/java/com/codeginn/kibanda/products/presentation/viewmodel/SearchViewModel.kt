package com.codeginn.kibanda.products.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeginn.kibanda.products.domain.model.Product
import com.codeginn.kibanda.products.domain.repository.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchViewModel(
    private val productRepository: ProductRepository
): ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private var searchJob: Job? = null

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        searchJob?.cancel() // Cancel previous search if text changes quickly
        if (text.isNotBlank()) {
            searchJob = viewModelScope.launch {
                productRepository.searchProducts(text).collectLatest { results ->
                    _searchResults.value = results
                }
            }
        } else {
            _searchResults.value = emptyList()
        }
    }
}