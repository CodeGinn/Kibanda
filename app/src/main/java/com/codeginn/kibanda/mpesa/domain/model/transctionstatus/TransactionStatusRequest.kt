package com.codeginn.kibanda.mpesa.domain.model.transctionstatus


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionStatusRequest(
    @SerialName("CommandID")
    val commandID: String,
    @SerialName("IdentifierType")
    val identifierType: Int,
    @SerialName("Initiator")
    val initiator: String,
    @SerialName("Occassion")
    val occassion: String,
    @SerialName("PartyA")
    val partyA: Int,
    @SerialName("QueueTimeOutURL")
    val queueTimeOutURL: String,
    @SerialName("Remarks")
    val remarks: String,
    @SerialName("ResultURL")
    val resultURL: String,
    @SerialName("SecurityCredential")
    val securityCredential: String,
    @SerialName("TransactionID")
    val transactionID: String
)