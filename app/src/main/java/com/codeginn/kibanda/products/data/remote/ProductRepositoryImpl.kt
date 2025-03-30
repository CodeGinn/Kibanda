package com.codeginn.kibanda.products.data.remote

import android.R.attr.category
import android.util.Log
import com.codeginn.kibanda.products.domain.model.Product
import com.codeginn.kibanda.products.domain.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ProductRepositoryImpl(
    private val productDB: FirebaseFirestore
): ProductRepository {

    val fruitsCollectionRef = productDB.collection("fruits")
    val vegetablesCollectionRef = productDB.collection("vegetables")

    override fun searchProducts(query: String): Flow<List<Product>> = flow{
        val fruits = searchCollection("fruits", query)
        val vegetables = searchCollection("vegetables", query)

        emit(fruits + vegetables)
    }

    private suspend fun searchCollection(collectionName: String, query: String): List<Product> {
        return try {
            val snapshot = productDB.collection(collectionName)
                .whereGreaterThanOrEqualTo("productName", query)
                .whereLessThan("productName", query + "\uf8ff") // Prefix matching
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(Product::class.java)?.apply {
                    this.productID = document.id
                }
            }
        } catch (e: Exception) {
            // Handle error appropriately (e.g., log it)
            Log.d("Search Result", "Error: $e")
            emptyList()
        }
    }

    override fun getAllFruits(): Flow<List<Product>> {
        return callbackFlow {
            val fruitsSnapshotListener = fruitsCollectionRef.addSnapshotListener { fruitsSnapshot, error ->
                if (error != null){
                    close(error)
                    return@addSnapshotListener
                }
                if (fruitsSnapshot != null){
                    val fruitsResult = fruitsSnapshot.toObjects(Product::class.java)
                    trySend(fruitsResult).isSuccess
                    Log.d("Result", "$fruitsResult")
                }
            }
            awaitClose{fruitsSnapshotListener.remove()}
        }
    }

    override fun getAllVegetables(): Flow<List<Product>> {
        return callbackFlow {
            val vegetablesSnapshotListener = vegetablesCollectionRef.addSnapshotListener { vegetableSnapshot, error ->
                if (error != null){
                    close(error)
                    return@addSnapshotListener
                }
                if (vegetableSnapshot != null){
                    val vegetablesResult = vegetableSnapshot.toObjects<Product>(Product::class.java)
                    trySend(vegetablesResult).isSuccess
                }

            }
            awaitClose{vegetablesSnapshotListener.remove()}
        }
    }
}