package com.codeginn.kibanda.cartcheckout.domain.model


data class CartItem(
    val itemId: String = "",
    val itemName: String = "",
    val itemImage: String = "",
    val itemPrice: Int = 0,
    val itemQuantity: Int = 1
){
    val totalItemPrice get() = itemPrice * itemQuantity
}
