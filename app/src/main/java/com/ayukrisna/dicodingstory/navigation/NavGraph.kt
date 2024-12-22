package com.ayukrisna.dicodingstory.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.ayukrisna.dicodingstory.view.ui.screen.story.addstory.AddStoryScreen
import com.ayukrisna.dicodingstory.view.ui.screen.story.detailstory.DetailStoryScreen
import com.ayukrisna.dicodingstory.view.ui.screen.story.liststory.ListStoryScreen
import com.ayukrisna.dicodingstory.view.ui.screen.auth.login.LoginScreen
import com.ayukrisna.dicodingstory.view.ui.screen.auth.signup.SignupScreen
import com.ayukrisna.dicodingstory.view.ui.screen.auth.splash.SplashScreen
import com.ayukrisna.dicodingstory.view.ui.screen.auth.welcome.WelcomeScreen
import com.ayukrisna.dicodingstory.view.ui.screen.maps.MapScreen

@Composable
fun NavGraph (
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController,
        startDestination = RootScreen.AuthNav,
        enterTransition = { fadeIn(tween(100))},
        popEnterTransition = {EnterTransition.None},
        exitTransition = { fadeOut(tween(100))},
        popExitTransition = {ExitTransition.None}
    ) {
        authNavGraph(navController)
        storyNavGraph(navController)
        mapNavGraph(navController)
    }
}

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
) {
    navigation<RootScreen.AuthNav>(
        startDestination = AuthScreen.SplashScreen
    ){
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
            val context = LocalContext.current
            val activity = context as? Activity
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(AuthScreen.LoginScreen)
                },
                onNavigateToSignup = {
                    navController.navigate(AuthScreen.SignupScreen)
                },
            )
            BackHandler {
                activity?.finish()
            }
        }

        composable<AuthScreen.LoginScreen> {
            val context = LocalContext.current
            val activity = context as? Activity
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
            BackHandler {
                activity?.finish()
            }
        }
        composable<AuthScreen.SignupScreen> {
            SignupScreen(
                onNavigateToLogin = {
                    navController.navigate(AuthScreen.LoginScreen)
                }
            )
        }
    }
}

fun NavGraphBuilder.storyNavGraph(
    navController: NavHostController,
) {
    navigation<RootScreen.StoryNav>(
        startDestination = StoryScreen.ListStoryScreen
    ) {
        composable<StoryScreen.ListStoryScreen>{
            val context = LocalContext.current
            val activity = context as? Activity
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
            BackHandler {
                activity?.finish()
            }
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
                onBackClick = {
                    navController.navigate(StoryScreen.ListStoryScreen) {
                        popUpTo(StoryScreen.AddStoryScreen) { inclusive = true }
                    }
                }
            )
        }
    }
}

fun NavGraphBuilder.mapNavGraph(
    navController: NavHostController,
) {
    navigation<RootScreen.MapNav>(
        startDestination = MapScreen.Map
    ) {
        composable<MapScreen.Map>{
            MapScreen()
        }
    }
}