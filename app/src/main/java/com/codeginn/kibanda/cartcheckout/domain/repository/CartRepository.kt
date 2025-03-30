package com.codeginn.kibanda.cartcheckout.domain.repository

import com.codeginn.kibanda.cartcheckout.domain.model.CartItem
import com.codeginn.kibanda.products.domain.model.Product
import com.codeginn.kibanda.utils.Result
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow <List<CartItem>>
    suspend fun addItemToCart(cartItem: Product)
    suspend fun removeItemFromCart(itemId: String)
    suspend fun clearCart()
    suspend fun updateQuantity(productId: String, quantity: Int)
}