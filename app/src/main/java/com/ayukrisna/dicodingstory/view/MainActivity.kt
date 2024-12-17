package com.ayukrisna.dicodingstory.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.ayukrisna.dicodingstory.MyApp
import com.ayukrisna.dicodingstory.navigation.NavGraph
import com.ayukrisna.dicodingstory.view.ui.MainScreen
import com.ayukrisna.dicodingstory.view.ui.theme.DicodingStoryTheme
import org.koin.core.annotation.KoinExperimentalAPI

class MainActivity : ComponentActivity() {
    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DicodingStoryTheme {
                MyApp()
                MainScreen()
            }
        }
    }
}
