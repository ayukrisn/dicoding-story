package com.ayukrisna.dicodingstory.domain.usecase

import com.ayukrisna.dicodingstory.domain.repository.UserRepository

class LogoutUseCase(private val userRepository: UserRepository) {
    suspend fun execute() {
        userRepository.logout()
    }
}