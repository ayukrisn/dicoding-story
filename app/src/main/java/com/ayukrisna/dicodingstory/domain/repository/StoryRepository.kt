package com.ayukrisna.dicodingstory.domain.repository

import androidx.paging.PagingData
import com.ayukrisna.dicodingstory.data.remote.response.AddStoryResponse
import com.ayukrisna.dicodingstory.data.remote.response.DetailStoryResponse
import com.ayukrisna.dicodingstory.data.remote.response.ListStoryItem
import com.ayukrisna.dicodingstory.data.remote.response.ListStoryResponse
import com.ayukrisna.dicodingstory.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface StoryRepository {
    fun getSession(): Flow<UserModel>
    suspend fun getStories() : Flow<PagingData<ListStoryItem>>
    suspend fun getStoriesWithLocation() : ListStoryResponse
    suspend fun getDetailStory(id: String) : DetailStoryResponse
    suspend fun addStory(description: String, photoUri: MultipartBody.Part, lat: Float? = null, lon: Float? = null) : AddStoryResponse
}