package com.ayukrisna.dicodingstory.view.ui.screen.auth.signup

import com.ayukrisna.dicodingstory.util.UiText

data class SignupState(
    val name: String = "",
    val nameError: UiText? = null,
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val isVisiblePassword: Boolean = false
)