package com.codeginn.kibanda.mpesa.utils

object MpesaEndPoints {
    const val AUTHARIZATION_ENDPOINT = "https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials"
    const val STKPUSH_ENDPOINT = "https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest"
    const val TRANSACTION_STATUS_ENDPOINT = "https://sandbox.safaricom.co.ke/mpesa/transactionstatus/v1/query"

}