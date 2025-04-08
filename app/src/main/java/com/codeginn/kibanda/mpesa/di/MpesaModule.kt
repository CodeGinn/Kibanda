package com.codeginn.kibanda.mpesa.di

import com.codeginn.kibanda.mpesa.data.MpesaApiServiceImpl
import com.codeginn.kibanda.mpesa.domain.service.MpesaApiService
import com.codeginn.kibanda.mpesa.presentation.viewmodel.MpesaViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.DefaultHttpRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.util.logging.Level

val mpesaModule = module {
    single {
        HttpClient(Android){
            install(ContentNegotiation){
                json(
                    Json{
                        ignoreUnknownKeys = true
                        prettyPrint = true
                    }
                )
            }
            install(Logging){
                logger = Logger.ANDROID
                level = LogLevel.ALL
            }
        }
    }
    singleOf(::MpesaApiServiceImpl).bind<MpesaApiService>()
    viewModelOf(::MpesaViewModel)
}