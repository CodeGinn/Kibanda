package com.codeginn.kibanda.authentication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeginn.kibanda.authentication.domain.model.AuthUserData
import com.codeginn.kibanda.authentication.domain.repository.AuthRepository
import com.codeginn.kibanda.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _userData = MutableStateFlow(AuthUserData())
    val userdata = _userData.asStateFlow()

    fun onFirstNameChange(firstname: String){
        _userData.update { it.copy(firstName = firstname) }
    }
    fun onLastNameChange(lastname: String){
        _userData.update { it.copy(lastName = lastname) }
    }
    fun onPhoneNumberChange(phone: String){
        _userData.update { it.copy(phoneNumber = phone) }
    }
    fun onEmailAdressChange(email: String){
        _userData.update { it.copy(emailAddress = email) }
    }


    fun signUp(user: AuthUserData, password: String) = viewModelScope.launch {
        authRepository.registerUser(user, password).collect { result ->
            when(result){
                is Resource.Loading<*> -> Resource.Loading()

                is Resource.Error<*> -> Resource.Error(result.toString())
                is Resource.Success<*> -> Resource.Success(result)
                else -> Resource.Idle()
            }
        }
    }

    fun login(email: String, password: String){
        viewModelScope.launch {
            authRepository.loginUser(email, password).collect { result ->
                when(result){
                    is Resource.Error<*> -> Resource.Error(result.toString())
                    is Resource.Loading<*> -> Resource.Loading()
                    is Resource.Success<*> -> Resource.Success(result)
                    else -> Resource.Idle()
                }
            }
        }
    }

    fun logout(){
        authRepository.logoutUser()
    }
}