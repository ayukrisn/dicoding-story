package com.ayukrisna.dicodingstory.domain.usecase

import com.ayukrisna.dicodingstory.data.remote.response.RegisterResponse
import com.ayukrisna.dicodingstory.domain.repository.UserRepository
import com.ayukrisna.dicodingstory.util.Result

class RegisterUseCase(private val userRepository: UserRepository) {
    suspend fun execute(name: String, email: String, password: String): Result<RegisterResponse> {
        return try {
            val response = userRepository.register(name, email, password)
            if (response.error == false) {
                Result.Success(response)
            } else {
                Result.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }
}