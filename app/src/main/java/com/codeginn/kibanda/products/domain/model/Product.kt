package com.codeginn.kibanda.products.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    var productID : String = "",
    val productName: String = "",
    val productUnit: String = "",
    val productUnitPrice: Int = 0,
    val productQuantity: Int = 1,
    val productAvailability: Boolean = true,
    val productImage: String = ""
)
