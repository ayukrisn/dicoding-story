package com.ayukrisna.dicodingstory.data.repository

import com.ayukrisna.dicodingstory.data.local.pref.UserPreference
import com.ayukrisna.dicodingstory.data.remote.response.LoginResponse
import com.ayukrisna.dicodingstory.data.remote.response.RegisterResponse
import com.ayukrisna.dicodingstory.data.remote.retrofit.ApiConfig
import com.ayukrisna.dicodingstory.domain.model.UserModel
import com.ayukrisna.dicodingstory.domain.repository.UserRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class UserRepositoryImp (
    private val userPreference: UserPreference
) : UserRepository {

    override suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    override fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    override suspend fun logout() {
        userPreference.logout()
    }

//    override suspend fun login(email: String, password: String): LoginResponse {
//        val apiService = ApiConfig.getApiService()
//        return apiService.loginUser(email, password)
//    }

    override suspend fun login(email: String, password: String): LoginResponse {
        val apiService = ApiConfig.getApiService()
        val response = apiService.loginUser(email, password)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response body is null")
        } else {
            val errorBody = response.errorBody()?.string()
            val errorResponse = errorBody?.let { parseErrorBody(it) }
            throw Exception(errorResponse?.message ?: "HTTP ${response.code()} error")
        }
    }

    override suspend fun register(name:String, email: String, password: String): RegisterResponse {
        val apiService = ApiConfig.getApiService()
        val response = apiService.registerUser(name, email, password)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response body is null")
        } else {
            val errorBody = response.errorBody()?.string()
            val errorResponse = errorBody?.let { parseErrorBody(it) }
            throw Exception(errorResponse?.message ?: "HTTP ${response.code()} error")
        }
    }

    private fun parseErrorBody(errorBody: String): LoginResponse? {
        return try {
            Gson().fromJson(errorBody, LoginResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }
}