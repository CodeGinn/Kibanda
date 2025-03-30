package com.codeginn.kibanda.navigation.mainscreennav

import android.os.Bundle
import androidx.navigation.NavType
import com.codeginn.kibanda.products.domain.model.Product
import kotlinx.serialization.json.Json
import android.net.Uri
import com.codeginn.kibanda.cartcheckout.domain.model.CartItem

object CustomNavType {
    val ProductType = object : NavType<Product>(
        isNullableAllowed = false
    ){
        override fun get(
            bundle: Bundle,
            key: String,
        ): Product? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Product {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Product): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: Product,
        ) {
            bundle.putString(key, Json.encodeToString(value))

        }

    }
    val CartItemType = object : NavType<CartItem>(
        isNullableAllowed = false
    ){
        override fun get(
            bundle: Bundle,
            key: String,
        ): CartItem? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): CartItem {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: CartItem): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: CartItem,
        ) {
            bundle.putString(key, Json.encodeToString(value))

        }

    }
}