package com.ayukrisna.dicodingstory.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ayukrisna.dicodingstory.MyApp
import com.ayukrisna.dicodingstory.view.ui.MainScreen
import com.ayukrisna.dicodingstory.view.ui.theme.DicodingStoryTheme

class MainActivity : ComponentActivity() {
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
