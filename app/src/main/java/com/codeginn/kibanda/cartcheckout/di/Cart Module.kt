package com.codeginn.kibanda.cartcheckout.di

import com.codeginn.kibanda.cartcheckout.data.CartRepositoryImpl
import com.codeginn.kibanda.cartcheckout.domain.repository.CartRepository
import com.codeginn.kibanda.cartcheckout.presentation.viewmodel.CartViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val cartModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }
    singleOf(::CartRepositoryImpl).bind<CartRepository>()
    viewModelOf(::CartViewModel)
}