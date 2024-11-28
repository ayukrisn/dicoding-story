package com.ayukrisna.dicodingstory.domain.usecase

import com.ayukrisna.dicodingstory.data.remote.response.Story
import com.ayukrisna.dicodingstory.domain.repository.StoryRepository
import com.ayukrisna.dicodingstory.util.Result

class DetailStoryUseCase(private val storyRepository: StoryRepository) {
    suspend fun execute(id: String): Result<Story> {
        return try {
            val response = storyRepository.getDetailStory(id)
            if (response.error == false) {
                val getDetailStory = response.story
                if (getDetailStory != null) {
                    Result.Success(getDetailStory)
                } else {
                    Result.Error("Detail story is null")
                }
            } else {
                Result.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }
}