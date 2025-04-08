package com.codeginn.kibanda.orders.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeginn.kibanda.cartcheckout.domain.model.CartItem
import com.codeginn.kibanda.orders.domain.model.Order
import com.codeginn.kibanda.orders.domain.repository.OrderRepository
import com.codeginn.kibanda.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderRepository: OrderRepository
): ViewModel() {
    private val _userOrders = MutableStateFlow<Resource<List<Order>>>(Resource.Loading())
    val userOrders = _userOrders.asStateFlow()

    private val _placeOrderResult = MutableStateFlow<Resource<String>?>(null)
    val placeOrderResult = _placeOrderResult.asStateFlow()

    private val _updateStatusResult = MutableStateFlow<Resource<Unit>?>(null)
    val updateStatusResult = _updateStatusResult.asStateFlow()

    init {
        fetchUserOrders()
    }

    fun fetchUserOrders() {
        viewModelScope.launch {
            orderRepository.getUserOrders().collectLatest { resource ->
                _userOrders.value = resource
            }
        }
    }
    fun placeNewOrder(cartItems: List<CartItem>) {
        viewModelScope.launch {
            _placeOrderResult.value = Resource.Loading()
            val result = orderRepository.placeOrder(cartItems)
            _placeOrderResult.value = result
        }
    }

    fun updateOrderDeliveryStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            _updateStatusResult.value = Resource.Loading()
            orderRepository.updateDeliveryStatus(orderId, newStatus)
            _updateStatusResult.value = Resource.Success(Unit) // Indicate success
        }
    }
    fun clearPlaceOrderResult() {
        _placeOrderResult.value = null
    }

    fun clearUpdateStatusResult() {
        _updateStatusResult.value = null
    }
}