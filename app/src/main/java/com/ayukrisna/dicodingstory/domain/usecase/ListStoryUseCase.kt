package com.ayukrisna.dicodingstory.domain.usecase

import com.ayukrisna.dicodingstory.data.remote.response.ListStoryItem
import com.ayukrisna.dicodingstory.domain.repository.StoryRepository
import com.ayukrisna.dicodingstory.util.Result

class ListStoryUseCase(private val storyRepository: StoryRepository) {
    suspend fun execute(): Result<List<ListStoryItem>> {
        return try {
            val response = storyRepository.getStories()
            if (response.error == false) {
                val getStoriesResult = response.listStory?.filterNotNull()
                if (getStoriesResult != null) {
                    Result.Success(getStoriesResult)
                } else {
                    Result.Error("List Story is null")
                }
            } else {
                Result.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }
}