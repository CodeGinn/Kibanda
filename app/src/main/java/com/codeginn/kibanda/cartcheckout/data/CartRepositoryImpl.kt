package com.codeginn.kibanda.cartcheckout.data

import android.util.Log
import com.codeginn.kibanda.cartcheckout.domain.model.CartItem
import com.codeginn.kibanda.cartcheckout.domain.repository.CartRepository
import com.codeginn.kibanda.products.domain.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CartRepositoryImpl(
    private val cartStore : FirebaseFirestore,
    private val cartAuth : FirebaseAuth
): CartRepository {
// Get Cart Items from Firestore
    private val cartCollection = cartStore.collection("carts")
    override fun getCartItems(): Flow <List<CartItem>> = callbackFlow{
        cartAuth.currentUser?.uid?.let {userId ->
            val listener = cartCollection.document(userId).collection("items")
                .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null){
                    val items = snapshot.toObjects(CartItem::class.java)
                    trySend(items).isSuccess
                }
            }
            awaitClose { listener.remove() }
        }


    }
// Add an Item to the Cart
    override suspend fun addItemToCart(item: Product){
        cartAuth.currentUser?.uid?.let {userId ->
            try {
                val cartItem = CartItem(
                    itemId = item.productID,
                    itemName = item.productName,
                    itemImage = item.productImage,
                    itemPrice = item.productUnitPrice
                )

                val existingCartItem = cartCollection.document(userId).collection("items")
                    .whereEqualTo("itemId", item.productID)
                    .get().await()
                    .documents.firstOrNull()
                if (existingCartItem != null){
                    val currentQuantity = existingCartItem.getLong("itemQuantity")
                    cartCollection.document(userId).collection("items").document(existingCartItem.id)
                        .update("itemQuantity", currentQuantity?.plus(1))
                } else {
                    cartCollection.document(userId).collection("items").document(item.productID)
                        .set(cartItem)
                }
            } catch (e: Exception){
                Log.d("Add Item Error", "$e")
            }
        }
    }
// Remove Item from the Cart
    override suspend fun removeItemFromCart(itemId: String) {
        cartAuth.currentUser?.uid?.let { userID ->
            cartCollection.document(userID).collection("items").document(itemId)
                .delete()
                .await()
        }

    }

    override suspend fun clearCart() {
        cartAuth.currentUser?.uid?.let { userId ->
            cartCollection.document(userId).collection("items")
                .get().await().documents.let { documents ->
                val batch = cartStore.batch()
                documents.forEach { document ->
                    batch.delete(document.reference)
                }
                batch.commit().await()
            }
        }
    }

    override suspend fun updateQuantity(productId: String, newQuantity: Int) {
        cartAuth.currentUser?.uid?.let { userId ->
            cartCollection.document(userId).collection("items").document(productId)
                .update("itemQuantity", newQuantity)
                .await()
        }
    }

}