package com.codeginn.kibanda.authentication.domain.repository

import com.codeginn.kibanda.authentication.domain.model.AuthUserData
import com.codeginn.kibanda.utils.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun getCurrentUser(): FirebaseUser?

    fun registerUser(user: AuthUserData, password: String): Flow<Resource<AuthUserData>>

    fun loginUser(email: String, password: String): Flow<Resource<AuthUserData>>

    fun logoutUser()




}