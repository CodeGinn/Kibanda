package com.codeginn.kibanda.products.di

import com.codeginn.kibanda.cartcheckout.presentation.viewmodel.CartViewModel
import com.codeginn.kibanda.products.data.remote.ProductRepositoryImpl
import com.codeginn.kibanda.products.domain.repository.ProductRepository
import com.codeginn.kibanda.products.presentation.viewmodel.ProductViewModel
import com.codeginn.kibanda.products.presentation.viewmodel.SearchViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val productModule = module {
    single { Firebase.firestore }
    singleOf(::ProductRepositoryImpl).bind<ProductRepository>()
    viewModelOf(::ProductViewModel)
    viewModelOf(::SearchViewModel)
}