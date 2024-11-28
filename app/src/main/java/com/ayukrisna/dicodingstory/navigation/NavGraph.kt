package com.ayukrisna.dicodingstory.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.toRoute
import com.ayukrisna.dicodingstory.view.ui.screen.addstory.AddStoryScreen
import com.ayukrisna.dicodingstory.view.ui.screen.detailstory.DetailStoryScreen
import com.ayukrisna.dicodingstory.view.ui.screen.liststory.ListStoryScreen
import com.ayukrisna.dicodingstory.view.ui.screen.login.LoginScreen
import com.ayukrisna.dicodingstory.view.ui.screen.signup.SignupScreen
import com.ayukrisna.dicodingstory.view.ui.screen.splash.SplashScreen
import com.ayukrisna.dicodingstory.view.ui.screen.welcome.WelcomeScreen

@Composable
fun NavGraph (
    navController: NavHostController,
) {
    NavHost(
        navController,
        startDestination = AuthScreen.SplashScreen,
        enterTransition = { fadeIn(tween(100))},
        popEnterTransition = {EnterTransition.None},
        exitTransition = { fadeOut(tween(100))},
        popExitTransition = {ExitTransition.None}
    ) {
        composable<AuthScreen.SplashScreen> {
            SplashScreen(
                onNavigateToWelcome = {
                    navController.navigate(AuthScreen.WelcomeScreen) {
                        popUpTo(AuthScreen.SplashScreen) { inclusive = true }
                    }
                },
                onNavigateToListStory = {
                    navController.navigate(StoryScreen.ListStoryScreen) {
                        popUpTo(AuthScreen.SplashScreen) { inclusive = true }
                    }
                }
            )
        }
        composable<AuthScreen.WelcomeScreen> {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(AuthScreen.LoginScreen)
                },
                onNavigateToSignup = {
                    navController.navigate(AuthScreen.SignupScreen)
                },
            )
        }
        composable<AuthScreen.LoginScreen> {
            LoginScreen(
                onNavigateToSignup = {
                    navController.navigate(AuthScreen.SignupScreen)
                },
                onNavigateToListStory = {
                    navController.navigate(StoryScreen.ListStoryScreen) {
                        popUpTo(AuthScreen.LoginScreen) { inclusive = true }
                    }
                }
            )
        }
        composable<AuthScreen.SignupScreen> {
            SignupScreen(
                onNavigateToLogin = {
                    navController.navigate(AuthScreen.LoginScreen)
                }
            )
        }
        composable<StoryScreen.ListStoryScreen>{
            ListStoryScreen(
                onNavigateToAddStory = {
                  navController.navigate(StoryScreen.AddStoryScreen)
                },
                onClick = { id ->
                    navController.navigate(StoryScreen.DetailStoryScreen(id))
                },
                onLogOut = {
                    navController.navigate(AuthScreen.WelcomeScreen) {
                        popUpTo(StoryScreen.ListStoryScreen) { inclusive = true }
                    }
                }
            )
        }
        composable<StoryScreen.DetailStoryScreen>{ entry ->
            val detailStory = entry.toRoute<StoryScreen.DetailStoryScreen>()
            DetailStoryScreen(
                id = detailStory.id,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable<StoryScreen.AddStoryScreen>{
            AddStoryScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}