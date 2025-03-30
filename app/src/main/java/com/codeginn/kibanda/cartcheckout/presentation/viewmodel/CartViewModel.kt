package com.codeginn.kibanda.cartcheckout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeginn.kibanda.cartcheckout.domain.model.CartItem
import com.codeginn.kibanda.cartcheckout.domain.repository.CartRepository
import com.codeginn.kibanda.products.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository
): ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    private val _totalAmount = MutableStateFlow(0)
    val totalAmount = _totalAmount.asStateFlow()

    private val _deliveryFee = MutableStateFlow(0.0)
    val deliveryFee = _deliveryFee.asStateFlow()

    private val _subtotal = MutableStateFlow(0)
    val subtotal = _subtotal

    init {
        fetchCartItems()
    }
    private fun fetchCartItems(){
        viewModelScope.launch {
            cartRepository.getCartItems().collect {items ->
                _cartItems.value= items
                calculateTotals(items)
            }

        }
    }

    fun addToCart(cartItem: Product){
        viewModelScope.launch {
            cartRepository.addItemToCart(cartItem)
        }
    }

    fun removeFromCart(cartItemID: String){
        viewModelScope.launch {
            cartRepository.removeItemFromCart(cartItemID)
        }
    }

    fun clearCart(){
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }
    fun updateQuantity(cartItemID: String, quantity: Int){
        viewModelScope.launch {
            cartRepository.updateQuantity(cartItemID, quantity)
        }
    }

    private fun calculateTotals(items: List<CartItem>) {
        val subtotal = items.sumOf { it.totalItemPrice}
        _subtotal.value = subtotal
        _deliveryFee.value = subtotal * 0.1
        _totalAmount.value = subtotal + _deliveryFee.value.toInt()
    }





}