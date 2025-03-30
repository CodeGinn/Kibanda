package com.codeginn.kibanda.mpesa.domain.service

import com.codeginn.kibanda.mpesa.domain.model.authorization.AuthorizationResponse
import com.codeginn.kibanda.mpesa.domain.model.lipanampesastkpush.MpesaSTKPushResponse

interface MpesaApiService {

    suspend fun getAccessToken(): String?

    suspend fun initiateSTKPush(phoneNumber: Long, paymentAmount: Int): MpesaSTKPushResponse
}