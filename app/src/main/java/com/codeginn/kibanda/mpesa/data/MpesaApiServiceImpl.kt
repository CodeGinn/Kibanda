package com.codeginn.kibanda.mpesa.data

import android.util.Base64
import com.codeginn.kibanda.mpesa.domain.model.authorization.AuthorizationResponse
import com.codeginn.kibanda.mpesa.domain.model.lipanampesastkpush.MpesaSTKPushResponse
import com.codeginn.kibanda.mpesa.domain.service.MpesaApiService
import com.codeginn.kibanda.mpesa.utils.MpesaEndPoints
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode

class MpesaApiServiceImpl(
    private val ktorClient : HttpClient
): MpesaApiService {

//    Authorization
    private val consumerKey = "Aa2CyihXlAAaMYBA35QBBJX1W384Qan0i0ioq8OJNlWqobSz"
    private val consumerSecret = "2qZ9AFg1q9wedgZLhtjPAkpEGwW7U58wIaAGvcSGkG6LmaZuSzW9IbqOxN0yTWOC"



    override suspend fun getAccessToken(): String? {
        val credential = "$consumerKey:$consumerSecret"
        val encodedCredential = Base64.encodeToString(credential.toByteArray(), Base64.NO_WRAP)
        return try {
            val response = ktorClient.get {
                url(MpesaEndPoints.AUTHARIZATION_ENDPOINT)
                headers {
                    append(HttpHeaders.Authorization, "Basic $encodedCredential")
                    append(HttpHeaders.Accept, ContentType.Application.Json.toString())
                }
            }
            if (response.status == HttpStatusCode.OK){
                val tokenResponse = response.body<AuthorizationResponse>()
                tokenResponse.accessToken
            } else null

        } catch (e: Exception){
            e.printStackTrace()
            null
        }

    }

//    STKPush


    override suspend fun initiateSTKPush(
        phoneNumber: Long,
        paymentAmount: Int,
    ): MpesaSTKPushResponse {
        TODO("Not yet implemented")
    }

//    Transaction Status
}