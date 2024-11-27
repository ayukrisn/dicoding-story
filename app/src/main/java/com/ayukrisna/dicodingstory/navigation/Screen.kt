package com.ayukrisna.dicodingstory.navigation

import kotlinx.serialization.Serializable

//@Serializable
//sealed class RootScreen {
//    @Serializable
//    object AuthNav
//}

@Serializable
sealed class AuthScreen {
    @Serializable
    data object SplashScreen : AuthScreen()

    @Serializable
    data object WelcomeScreen : AuthScreen()

    @Serializable
    data object LoginScreen : AuthScreen()

    @Serializable
    data object SignupScreen : AuthScreen()
}

@Serializable
sealed class StoryScreen {
    @Serializable
    data object ListStoryScreen : StoryScreen()

    @Serializable
    data class DetailStoryScreen(val id: String) : StoryScreen()
}