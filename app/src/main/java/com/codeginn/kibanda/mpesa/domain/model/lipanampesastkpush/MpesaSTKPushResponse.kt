package com.codeginn.kibanda.mpesa.domain.model.lipanampesastkpush


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MpesaSTKPushResponse(
    @SerialName("CheckoutRequestID")
    val checkoutRequestID: String,
    @SerialName("CustomerMessage")
    val customerMessage: String,
    @SerialName("MerchantRequestID")
    val merchantRequestID: String,
    @SerialName("ResponseCode")
    val responseCode: String,
    @SerialName("ResponseDescription")
    val responseDescription: String
)