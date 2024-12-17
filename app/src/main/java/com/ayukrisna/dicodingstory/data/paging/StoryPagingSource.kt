package com.ayukrisna.dicodingstory.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ayukrisna.dicodingstory.data.remote.response.ListStoryItem
import com.ayukrisna.dicodingstory.data.remote.retrofit.ApiService

class StoryPagingSource(
    private val apiService: ApiService,
) : PagingSource<Int, ListStoryItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getStories(position, params.loadSize)

            if (response.isSuccessful) {
                val storyList = response.body()?.listStory?.filterNotNull() ?: emptyList()
                LoadResult.Page(
                    data = storyList,
                    prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                    nextKey = if (storyList.isEmpty()) null else position + 1
                )
            } else {
                LoadResult.Error(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }



}