package com.ayukrisna.dicodingstory.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import com.ayukrisna.dicodingstory.data.local.database.StoryDatabase
import com.ayukrisna.dicodingstory.data.local.pref.UserPreference
import com.ayukrisna.dicodingstory.data.paging.StoryPagingSource
import com.ayukrisna.dicodingstory.data.paging.StoryRemoteMediator
import com.ayukrisna.dicodingstory.data.remote.response.AddStoryResponse
import com.ayukrisna.dicodingstory.data.remote.response.DetailStoryResponse
import com.ayukrisna.dicodingstory.data.remote.response.ListStoryItem
import com.ayukrisna.dicodingstory.data.remote.response.ListStoryResponse
import com.ayukrisna.dicodingstory.data.remote.retrofit.ApiConfig
import com.ayukrisna.dicodingstory.domain.model.UserModel
import com.ayukrisna.dicodingstory.domain.repository.StoryRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


class StoryRepositoryImp (
    private val userPreference: UserPreference,
    private val database: StoryDatabase
) : StoryRepository {
    override fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getStories(): Flow<PagingData<ListStoryItem>> {
        val token = userPreference.getSession().first().token
        val apiService = ApiConfig.getApiService(token)

        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            remoteMediator = StoryRemoteMediator(database, apiService),
            pagingSourceFactory = {  database.storyDao().getAllStory() }
        ).flow
    }

    override suspend fun getStoriesWithLocation(): ListStoryResponse {
        val token = userPreference.getSession().first().token
        val apiService = ApiConfig.getApiService(token)
        val response = apiService.getStoriesWithLocation()

        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response body is null")
        } else {
            val errorBody = response.errorBody()?.string()
            val errorResponse = errorBody?.let { parseErrorBody(it) }
            throw Exception(errorResponse?.message ?: "HTTP ${response.code()} error")
        }
    }

    override suspend fun getDetailStory(id: String): DetailStoryResponse {
        val token = userPreference.getSession().first().token
        val apiService = ApiConfig.getApiService(token)
        val response = apiService.getDetailStory(id)

        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response body is null")
        } else {
            val errorBody = response.errorBody()?.string()
            val errorResponse = errorBody?.let { parseErrorBody(it) }
            throw Exception(errorResponse?.message ?: "HTTP ${response.code()} error")
        }
    }

    override suspend fun addStory(
        description: String,
        photoUri: MultipartBody.Part,
        lat: Float?,
        lon: Float?
    ): AddStoryResponse {
        val token = userPreference.getSession().first().token
        val apiService = ApiConfig.getApiService(token)

        val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val response = apiService.addStory(descriptionBody, photoUri, lat, lon)

        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response body is null")
        } else {
            val errorBody = response.errorBody()?.string()
            val errorResponse = errorBody?.let { parseErrorBody(it) }
            throw Exception(errorResponse?.message ?: "HTTP ${response.code()} error")
        }
    }

    private fun parseErrorBody(errorBody: String): ListStoryResponse? {
        return try {
            Gson().fromJson(errorBody, ListStoryResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }
}