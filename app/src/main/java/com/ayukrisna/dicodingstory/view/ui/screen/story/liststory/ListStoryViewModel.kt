package com.ayukrisna.dicodingstory.view.ui.screen.story.liststory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ayukrisna.dicodingstory.data.remote.response.ListStoryItem
import com.ayukrisna.dicodingstory.domain.usecase.ListStoryUseCase
import com.ayukrisna.dicodingstory.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListStoryViewModel (
    private val listStoryUseCase: ListStoryUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _stories = MutableStateFlow<PagingData<ListStoryItem>>(PagingData.empty())
    val stories: StateFlow<PagingData<ListStoryItem>> = _stories

    init {
        loadStory()
    }

    fun loadStory() {
        viewModelScope.launch {
            listStoryUseCase.loadStory()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _stories.value = pagingData
                }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            logoutUseCase.execute()
        }
    }
}