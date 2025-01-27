package com.ayukrisna.dicodingstory.domain.repository

import com.ayukrisna.dicodingstory.data.remote.response.LoginResponse
import com.ayukrisna.dicodingstory.data.remote.response.RegisterResponse
import com.ayukrisna.dicodingstory.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveSession(user: UserModel)
    fun getSession(): Flow<UserModel>
    suspend fun register(name:String, email: String, password: String): RegisterResponse
    suspend fun login(email: String, password: String): LoginResponse
    suspend fun logout()
}