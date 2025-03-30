package com.codeginn.kibanda

import android.app.Application
import com.codeginn.kibanda.authentication.di.authModule
import com.codeginn.kibanda.cartcheckout.di.cartModule
import com.codeginn.kibanda.mpesa.di.networkModule
import com.codeginn.kibanda.orders.di.orderModule
import com.codeginn.kibanda.posts.di.postsModule
import com.codeginn.kibanda.products.di.productModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KibandaApplication: Application() {
    override fun onCreate() {
        super.onCreate()
// Initialize Firebase App
        FirebaseApp.initializeApp(this)

        startKoin {
            androidContext(this@KibandaApplication)
            modules(networkModule)
//            modules(authModule, productModule, cartModule, orderModule)
        }
    }
}