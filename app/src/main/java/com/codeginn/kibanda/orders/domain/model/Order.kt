package com.codeginn.kibanda.orders.domain.model

import com.codeginn.kibanda.cartcheckout.domain.model.CartItem
import java.security.Timestamp

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val items: List<CartItem> = emptyList(),
    val subtotal: Int = 0,
    val deliveryFee: Double = 0.0,
    val totalAmount: Int = 0,
    val orderDate: Long = System.currentTimeMillis(),
    var deliveryStatus: String = "Pending"
)
