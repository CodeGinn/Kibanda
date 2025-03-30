package com.codeginn.kibanda.posts.di

import android.util.Log
import com.codeginn.kibanda.posts.data.PostsServiceImpl
import com.codeginn.kibanda.posts.domain.repository.PostsService
import com.codeginn.kibanda.posts.presentation.viewmodel.PostsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HeadersBuilder
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val postsModule = module {
    single {
        HttpClient(Android){
            install(ContentNegotiation){
                json(Json{
                    isLenient = true
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
            install(Logging){
                level = LogLevel.ALL
                logger = object : Logger{
                    override fun log(message: String) {
                        Log.v("Ktor Client", message)
                    }

                }
            }
            install(DefaultRequest){
                header("Content-Type", "application/json")
            }
            install(HttpRequestRetry){
                maxRetries = 3
                exponentialDelay()
            }
        }
    }
    singleOf(::PostsServiceImpl).bind<PostsService>()
    viewModelOf(::PostsViewModel)
}