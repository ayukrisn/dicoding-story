package com.ayukrisna.dicodingstory.view.ui.screen.auth.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayukrisna.dicodingstory.domain.model.UserModel
import com.ayukrisna.dicodingstory.domain.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SplashViewModel (
    private val userRepository: UserRepository
) : ViewModel(){
    val userSession: StateFlow<UserModel> = userRepository.getSession()
        .map { user -> user }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserModel("", "", "", "", false))
}