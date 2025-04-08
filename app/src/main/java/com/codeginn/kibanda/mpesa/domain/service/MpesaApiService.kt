package com.codeginn.kibanda.mpesa.domain.service

import com.codeginn.kibanda.mpesa.domain.model.authorization.AuthorizationResponse
import com.codeginn.kibanda.mpesa.domain.model.lipanampesastkpush.MpesaSTKPushResponse
import com.codeginn.kibanda.mpesa.domain.model.transctionstatus.TransactionStatusResponse

interface MpesaApiService {

    suspend fun getAccessToken(): String?

    suspend fun initiateSTKPush(phoneNumber: Long, paymentAmount: Int): MpesaSTKPushResponse?

    suspend fun checkTransactionStatus(transactionId: String): TransactionStatusResponse?
}
