package com.ayukrisna.dicodingstory.view.ui.screen.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayukrisna.dicodingstory.domain.usecase.LoginUseCase
import com.ayukrisna.dicodingstory.domain.usecase.ValidateEmailUseCase
import com.ayukrisna.dicodingstory.domain.usecase.ValidatePasswordUseCase
import com.ayukrisna.dicodingstory.util.Result
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val validateEmailUseCase = ValidateEmailUseCase()
    private val validatePasswordUseCase = ValidatePasswordUseCase()

    var formState by mutableStateOf(LoginState())

    private val _loginState = MutableLiveData<Result<Unit>>(Result.Idle)
    val loginState: LiveData<Result<Unit>> = _loginState

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                formState = formState.copy(email = event.email)
                validateEmail()
            }

            is LoginEvent.PasswordChanged -> {
                formState = formState.copy(password = event.password)
                validatePassword()
            }

            is LoginEvent.VisiblePassword -> {
                formState = formState.copy(isVisiblePassword = event.isVisiblePassword)
            }

            is LoginEvent.Submit -> {
                val isEmailValid = validateEmail()
                val isPasswordValid = validatePassword()

                if (isEmailValid && isPasswordValid) {
                    login(formState.email, formState.password )
                }
            }
        }
    }

    private fun validateEmail(): Boolean {
        val emailResult = validateEmailUseCase.execute(formState.email)
        formState = formState.copy(emailError = emailResult.errorMessage)
        return emailResult.successful
    }

    private fun validatePassword(): Boolean {
        val passwordResult = validatePasswordUseCase.execute(formState.password)
        formState = formState.copy(passwordError = passwordResult.errorMessage)
        return passwordResult.successful
    }

    private fun login(email: String, password: String){
        viewModelScope.launch {
            _loginState.value = Result.Loading
            val result = loginUseCase.execute(email, password)
            _loginState.value = result
        }
    }
}