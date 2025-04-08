package com.codeginn.kibanda.orders.di

import com.codeginn.kibanda.orders.data.OrderRepositoryImpl
import com.codeginn.kibanda.orders.domain.repository.OrderRepository
import com.codeginn.kibanda.orders.presentation.viewmodel.OrderViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val orderModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }
    singleOf(::OrderRepositoryImpl).bind<OrderRepository>()
    viewModelOf(::OrderViewModel)
}