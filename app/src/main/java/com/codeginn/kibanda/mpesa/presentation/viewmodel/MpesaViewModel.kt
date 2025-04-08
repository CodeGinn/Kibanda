package com.codeginn.kibanda.mpesa.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeginn.kibanda.mpesa.domain.service.MpesaApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MpesaViewModel(
    private val mpesaApi : MpesaApiService
): ViewModel() {
    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber = _phoneNumber.asStateFlow()
    private val _amount = MutableStateFlow<Int>(100)
    val amount = _amount.asStateFlow()
    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken = _accessToken.asStateFlow()
    private val _transactionStatus = MutableStateFlow<String?>(null)
    val transactionStatus = _transactionStatus.asStateFlow()

    fun onPhoneChange(phone: String){
        _phoneNumber.apply {
            this.value = phone
        }
    }



    fun initiateSTKPayment(phoneNumber: Long, payableAmount: Int){
        viewModelScope.launch {
            _accessToken.value = mpesaApi.getAccessToken()
            _transactionStatus.value = mpesaApi.initiateSTKPush(phoneNumber, payableAmount)?.responseCode
        }
    }
}