package com.aksharadeepa.tutor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.aksharadeepa.tutor.ui.navigation.AppNavGraph
import com.aksharadeepa.tutor.ui.theme.AksharaDeepaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as AksharaDeepaApplication
        setContent {
            AksharaDeepaTheme {
                AppNavGraph(appContainer = app.container)
            }
        }
    }
}
