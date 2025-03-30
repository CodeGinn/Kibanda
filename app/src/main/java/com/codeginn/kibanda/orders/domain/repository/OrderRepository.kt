package com.codeginn.kibanda.orders.domain.repository

import com.codeginn.kibanda.cartcheckout.domain.model.CartItem
import com.codeginn.kibanda.orders.domain.model.Order
import com.codeginn.kibanda.utils.Resource
import com.codeginn.kibanda.utils.Result
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    fun getUserOrders(): Flow<Resource<List<Order>>>
    suspend fun placeOrder(cartItems: List<CartItem>): Resource<String>
    suspend fun updateDeliveryStatus(orderId: String, newStatus: String)
}