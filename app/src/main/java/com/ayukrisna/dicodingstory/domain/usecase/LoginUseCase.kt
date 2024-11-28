package com.ayukrisna.dicodingstory.domain.usecase

import com.ayukrisna.dicodingstory.data.remote.response.LoginResult
import com.ayukrisna.dicodingstory.domain.model.UserModel
import com.ayukrisna.dicodingstory.domain.repository.UserRepository
import com.ayukrisna.dicodingstory.util.Result

class LoginUseCase(private val userRepository: UserRepository) {
    suspend fun execute(email: String, password: String): Result<Unit> {
        return try {
            val response = userRepository.login(email, password)
            if (response.error == false) {
                val loginResult = response.loginResult
                    ?: return Result.Error("Login result is null")
                val userModel = createUserModel(loginResult, email)
                userRepository.saveSession(userModel)
                Result.Success(Unit) // Indicate success with no additional data
            } else {
                Result.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    private fun createUserModel(loginResult: LoginResult, email: String): UserModel {
        return UserModel(
            name = loginResult.name ?: throw IllegalArgumentException("Name is null"),
            email = email,
            id = loginResult.userId ?: throw IllegalArgumentException("User ID is null"),
            token = loginResult.token ?: throw IllegalArgumentException("Token is null"),
            isLogin = true
        )
    }
}