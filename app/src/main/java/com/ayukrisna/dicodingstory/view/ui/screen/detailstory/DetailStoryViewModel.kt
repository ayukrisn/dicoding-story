package com.ayukrisna.dicodingstory.view.ui.screen.detailstory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayukrisna.dicodingstory.data.remote.response.Story
import com.ayukrisna.dicodingstory.domain.usecase.DetailStoryUseCase
import com.ayukrisna.dicodingstory.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailStoryViewModel (
    private val detailStoryUseCase: DetailStoryUseCase
) : ViewModel() {
    private val _storyState = MutableStateFlow<Result<Story>>(Result.Idle)
    val storyState: StateFlow<Result<Story>> = _storyState

    fun fetchStory(id: String) {
        _storyState.value = Result.Loading

        viewModelScope.launch {
            val result = detailStoryUseCase.execute(id)
            _storyState.value = result
        }
    }
}