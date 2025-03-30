package com.codeginn.kibanda.orders.data

import com.codeginn.kibanda.cartcheckout.domain.model.CartItem
import com.codeginn.kibanda.orders.domain.model.Order
import com.codeginn.kibanda.orders.domain.repository.OrderRepository
import com.codeginn.kibanda.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

class OrderRepositoryImpl(
    private val orderStore: FirebaseFirestore,
    private val orderAuth: FirebaseAuth
) : OrderRepository {
    private val ordersCollection = orderStore.collection("orders")
    override fun getUserOrders(): Flow<Resource<List<Order>>> = callbackFlow{
        orderAuth.currentUser?.uid?.let { userId ->
            ordersCollection
                .whereEqualTo("userId", userId)
                .orderBy("orderDate", Query.Direction.DESCENDING)
                .snapshots()
                .map { snapshot ->
                    if (snapshot.isEmpty) {
                        Resource.Success(emptyList())
                    } else {
                        val orders = snapshot.documents.mapNotNull { it.toObject(Order::class.java) }
                        Resource.Success(orders)
                    }
                }
        }
    }

    override suspend fun placeOrder(cartItems: List<CartItem>): Resource<String> {
        return orderAuth.currentUser?.uid?.let { userId ->
            try {
                val subtotal = cartItems.sumOf { it.totalItemPrice }
                val deliveryFee = subtotal * 0.10
                val totalAmount = subtotal + deliveryFee.toInt()
                val orderId = UUID.randomUUID().toString()
                val order = Order(
                    orderId = orderId,
                    userId = userId,
                    items = cartItems,
                    subtotal = subtotal,
                    deliveryFee = deliveryFee,
                    totalAmount = totalAmount,
                    orderDate = System.currentTimeMillis()
                )
                ordersCollection.document(orderId).set(order).await()
                // Optionally clear the cart after placing the order
                // firestore.collection("carts").document(uid).collection("items").get().await().documents.forEach { it.reference.delete().await() }
                Resource.Success(orderId)
            } catch (e: Exception) {
                Resource.Error("Failed to place order: ${e.localizedMessage}")
            }
        } ?: Resource.Error("User not authenticated")
    }

    override suspend fun updateDeliveryStatus(orderId: String, newStatus: String) {
        try {
            ordersCollection.document(orderId).update("deliveryStatus", newStatus).await()
        } catch (e: Exception) {
            // Handle error
        }
    }
}