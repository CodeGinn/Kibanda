package com.codeginn.kibanda.authentication.di

import com.codeginn.kibanda.authentication.data.AuthRepositoryImpl
import com.codeginn.kibanda.authentication.domain.repository.AuthRepository
import com.codeginn.kibanda.authentication.presentation.viewmodel.AuthViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    viewModelOf(::AuthViewModel)
}