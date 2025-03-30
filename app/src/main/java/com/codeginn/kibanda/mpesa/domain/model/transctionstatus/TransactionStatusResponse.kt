package com.codeginn.kibanda.mpesa.domain.model.transctionstatus


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionStatusResponse(
    @SerialName("ConversationID")
    val conversationID: String,
    @SerialName("OriginatorConversationID")
    val originatorConversationID: String,
    @SerialName("ResponseCode")
    val responseCode: String,
    @SerialName("ResponseDescription")
    val responseDescription: String
)