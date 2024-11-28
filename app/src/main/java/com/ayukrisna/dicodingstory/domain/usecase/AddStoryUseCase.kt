package com.ayukrisna.dicodingstory.domain.usecase

import com.ayukrisna.dicodingstory.data.remote.response.AddStoryResponse
import com.ayukrisna.dicodingstory.domain.repository.StoryRepository
import com.ayukrisna.dicodingstory.util.Result
import okhttp3.MultipartBody

class AddStoryUseCase (private val storyRepository: StoryRepository){
    suspend fun execute(description: String, photoUri: MultipartBody.Part, lat: Float?, lon: Float?) : Result<AddStoryResponse>{
        return try {
            val response = storyRepository.addStory(description, photoUri, lat, lon)
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