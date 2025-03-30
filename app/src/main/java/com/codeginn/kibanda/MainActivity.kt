package com.codeginn.kibanda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.codeginn.kibanda.navigation.KibandaApp
import com.codeginn.kibanda.ui.theme.KibandaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KibandaTheme {
                KibandaApp()
            }
        }
    }
}
