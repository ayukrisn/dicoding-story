package com.ayukrisna.dicodingstory.view.ui.screen.auth.signup

sealed class SignupEvent {
    data class NameChanged(val name: String) : SignupEvent()
    data class EmailChanged(val email: String) : SignupEvent()
    data class PasswordChanged(val password: String) : SignupEvent()
    data class VisiblePassword(val isVisiblePassword: Boolean) : SignupEvent()
    object Submit : SignupEvent()
}