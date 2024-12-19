package com.ayukrisna.dicodingstory.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState.Loading.endOfPaginationReached
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.ayukrisna.dicodingstory.data.local.database.StoryDatabase
import com.ayukrisna.dicodingstory.data.remote.response.ListStoryItem
import com.ayukrisna.dicodingstory.data.remote.retrofit.ApiService
import androidx.room.withTransaction


@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX

        return try {
            val responseData = apiService.getStories(page, state.config.pageSize)
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.storyDao().deleteAll()
                }
                responseData.body()?.listStory?.filterNotNull()
                    ?.let { database.storyDao().insertStory(it) }
            }
            MediatorResult.Success(
                endOfPaginationReached = responseData.body()?.listStory.isNullOrEmpty()
            )
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }
}