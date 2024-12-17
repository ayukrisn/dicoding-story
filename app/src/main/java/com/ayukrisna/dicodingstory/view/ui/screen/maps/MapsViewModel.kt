package com.ayukrisna.dicodingstory.view.ui.screen.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayukrisna.dicodingstory.data.remote.response.ListStoryItem
import com.ayukrisna.dicodingstory.domain.usecase.ListStoryUseCase
import com.ayukrisna.dicodingstory.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapsViewModel(
    private val listStoryUseCase: ListStoryUseCase,
) : ViewModel() {
    private val _storiesState = MutableStateFlow<Result<List<ListStoryItem>>>(Result.Idle)
    val storiesState: StateFlow<Result<List<ListStoryItem>>> = _storiesState

    fun fetchStories() {
        _storiesState.value = Result.Loading

        viewModelScope.launch {
            val result = listStoryUseCase.loadWithLocation()
            _storiesState.value = result
        }
    }
}