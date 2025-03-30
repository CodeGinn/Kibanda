package com.codeginn.kibanda.authentication.domain.model

data class AuthUserData(
    val userID: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val emailAddress: String = "",
)
