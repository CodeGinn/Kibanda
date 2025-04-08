package com.codeginn.kibanda.mpesa.data

import android.util.Base64
import com.codeginn.kibanda.mpesa.domain.model.authorization.AuthorizationResponse
import com.codeginn.kibanda.mpesa.domain.model.lipanampesastkpush.MpesaSTKPushRequest
import com.codeginn.kibanda.mpesa.domain.model.lipanampesastkpush.MpesaSTKPushResponse
import com.codeginn.kibanda.mpesa.domain.model.transctionstatus.TransactionStatusRequest
import com.codeginn.kibanda.mpesa.domain.model.transctionstatus.TransactionStatusResponse
import com.codeginn.kibanda.mpesa.domain.service.MpesaApiService
import com.codeginn.kibanda.mpesa.utils.MpesaEndPoints
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    private val businessShortCode = 174379
    private val callBackUrl = "https://mydomain.com/path"
    private val passKey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"
    private val transactionDescription = "Payment For Groceries"
    private val transactionType = "CustomerPayBillOnline"
    private val accountRef = "Kibanda App"
    private val timeStamp = getTimeStamp()
    private val mpesaPassword = generatePassword()

    override suspend fun initiateSTKPush(
        phoneNumber: Long,
        paymentAmount: Int,
    ): MpesaSTKPushResponse? {
        val token = getAccessToken()

        val requestBody = MpesaSTKPushRequest(
            accountReference = accountRef,
            amount = paymentAmount,
            businessShortCode = businessShortCode,
            callBackURL = callBackUrl,
            partyA = phoneNumber,
            partyB = businessShortCode,
            password = mpesaPassword,
            phoneNumber = phoneNumber,
            timestamp = timeStamp,
            transactionDesc = transactionDescription,
            transactionType = transactionType
        )
        return try {
            val response = ktorClient.post {
                url(MpesaEndPoints.STKPUSH_ENDPOINT)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                }
                setBody(requestBody)
            }
            response.body()
        } catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    private fun getTimeStamp(): String {
        return DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())
    }

    private fun generatePassword(): String {
        return Base64.encodeToString("$businessShortCode$passKey$timeStamp".toByteArray(), Base64.NO_WRAP)
    }

//    Transaction Status
    private val identifierType = 4
    private val commandID = "TransactionStatusQuery"
    private val remarks = "Transaction Complete"
    private val resultUrl = "https://mydomain.com/TransactionStatus/result/"
    private val queueTimeout = "https://mydomain.com/TransactionStatus/queue/"
    private val initiator = "testapi"
    private val partyA = 600990
    private val occasion = "Ok"
//    private val passWord = "Safaricom123!!"
    private val secretCredential = "ClONZiMYBpc65lmpJ7nvnrDmUe0WvHvA5QbOsPjEo92B6IGFwDdvdeJIFL0kgwsEKWu6SQKG4ZZUxjC"



    override suspend fun checkTransactionStatus(transactionId: String): TransactionStatusResponse? {
        val token = getAccessToken()
        val requestBody = TransactionStatusRequest(
            commandID = commandID,
            identifierType = 4,
            initiator = initiator,
            occassion = occasion,
            partyA = partyA,
            queueTimeOutURL = queueTimeout,
            remarks = remarks,
            resultURL = resultUrl,
            securityCredential = secretCredential,
            transactionID = transactionId
        )
        return try {
            val response = ktorClient.post {
                url(MpesaEndPoints.TRANSACTION_STATUS_ENDPOINT)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                }
                setBody(requestBody)
            }
            response.body()

        } catch (e: Exception){
            e.printStackTrace()
            null
        }
    }
}