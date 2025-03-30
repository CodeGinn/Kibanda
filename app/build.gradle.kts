import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.codeginn.kibanda"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.codeginn.kibanda"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

//    Koin
    implementation(libs.bundles.koin)

//    Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)


//    Extended Material Icons
    implementation(libs.androidx.material.icons.extended)

//    Lifecycle Viewmodel
    implementation(libs.lifecycle.viewmodel.compose)

//    Navigation Compose
    implementation(libs.androidx.navigation.compose)
    
//    Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

//    Coil Image Loader
    implementation(libs.coil.compose)

//    Kotlinx Coroutines
    implementation(libs.kotlinx.coroutines.android)

//    Accompanist Pager
    implementation(libs.accompanist.pager)

//    Ktor Client
    implementation(libs.bundles.ktor.client)

    /*
//    Ktor Client Core
    implementation(libs.ktor.client.core)
//    Ktor Client Android
    implementation(libs.ktor.client.android)
//    Ktor Client JSON Support
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
//    Ktor Client Logging Interceptor
    implementation(libs.ktor.client.logging)
*/

}