package com.codeginn.kibanda.authentication.data

import android.util.Log
import com.codeginn.kibanda.authentication.domain.model.AuthUserData
import com.codeginn.kibanda.authentication.domain.repository.AuthRepository
import com.codeginn.kibanda.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val authDB: FirebaseFirestore
): AuthRepository {
    override fun getCurrentUser(): FirebaseUser? = auth.currentUser


    override fun registerUser(
        user: AuthUserData,
        password: String,
    ): Flow<Resource<AuthUserData>> {
        return flow {
            auth.createUserWithEmailAndPassword(user.emailAddress, password)
                .addOnSuccessListener {
                    it.user?.let {
                        val uid = it.uid
                        val userData = AuthUserData(userID = uid, firstName = user.firstName, lastName = user.lastName, emailAddress = user.emailAddress, phoneNumber = user.phoneNumber)
                        saveUserInfo(uid, userData)
                    }
                }.addOnFailureListener {
                    Resource.Error<String>(it.message.toString())
                }
        }
    }

    fun saveUserInfo(userId: String, user: AuthUserData){
        authDB.collection("users").document(userId).set(user)
            .addOnSuccessListener {
                Resource.Success(user)
            }.addOnFailureListener {
                Resource.Error<String>(it.message.toString())
            }
    }

    override fun loginUser(
        email: String,
        password: String,
    ): Flow<Resource<AuthUserData>> {
        return flow {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        Resource.Success(it)
                    }
                }.addOnFailureListener {
                    Resource.Error<String>(it.message.toString())
                }
        }
    }

    override fun logoutUser() {
        auth.signOut()
    }

}


