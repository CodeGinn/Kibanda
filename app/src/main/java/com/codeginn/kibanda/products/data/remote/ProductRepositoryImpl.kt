package com.codeginn.kibanda.products.data.remote

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
        try {
            val vegetablesSnapshot = vegetablesCollectionRef.orderBy("productName")
                .startAt(query).endAt(query + "\uf8ff").get().await()
            val fruitsSnapshot = fruitsCollectionRef.orderBy("productName")
                .startAt(query).endAt(query + "\uf8ff").get().await()

            val searchProducts = vegetablesSnapshot.toObjects(Product::class.java) +
                    fruitsSnapshot.toObjects(Product::class.java)

            emit(searchProducts)
        } catch (e: Exception){
            e.printStackTrace()
            emit(emptyList())
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