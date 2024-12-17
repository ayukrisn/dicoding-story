package com.ayukrisna.dicodingstory.view.ui

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ayukrisna.dicodingstory.navigation.BottomNavigationBar
import com.ayukrisna.dicodingstory.navigation.NavGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val noBarRoutes = setOf(
        "com.ayukrisna.dicodingstory.navigation.AuthScreen.SplashScreen",
        "com.ayukrisna.dicodingstory.navigation.AuthScreen.WelcomeScreen",
        "com.ayukrisna.dicodingstory.navigation.AuthScreen.LoginScreen",
        "com.ayukrisna.dicodingstory.navigation.AuthScreen.SignupScreen",

        "com.ayukrisna.dicodingstory.navigation.StoryScreen.DetailStoryScreen/{id}",
        "com.ayukrisna.dicodingstory.navigation.StoryScreen.AddStoryScreen",
    )

    Scaffold (
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val currentRoute = currentDestination?.route

            val showBottomBar = currentRoute !in noBarRoutes
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->

        NavGraph(
            navController = navController,
            paddingValues = innerPadding
        )
    }
}