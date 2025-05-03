package com.codeginn.kibanda.navigation.mainscreennav

import com.codeginn.kibanda.products.domain.model.Product
import kotlinx.serialization.Serializable

@Serializable data class ProductDetails(
    val product: Product
)



@Serializable object MpesaScreen

//@Serializable object OrderHistory