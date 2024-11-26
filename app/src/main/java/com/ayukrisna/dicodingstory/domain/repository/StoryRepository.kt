package com.ayukrisna.dicodingstory.domain.repository

import com.ayukrisna.dicodingstory.data.remote.response.ListStoryResponse
import com.ayukrisna.dicodingstory.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    fun getSession(): Flow<UserModel>
    suspend fun getStories() : ListStoryResponse
}