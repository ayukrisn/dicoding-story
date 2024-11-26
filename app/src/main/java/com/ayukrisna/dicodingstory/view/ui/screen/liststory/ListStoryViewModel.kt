package com.ayukrisna.dicodingstory.view.ui.screen.liststory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayukrisna.dicodingstory.data.remote.response.ListStoryItem
import com.ayukrisna.dicodingstory.domain.usecase.ListStoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import com.ayukrisna.dicodingstory.util.Result
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListStoryViewModel (
    private val listStoryUseCase: ListStoryUseCase
) : ViewModel() {
    private val _storiesState = MutableStateFlow<Result<List<ListStoryItem>>>(Result.Idle)
    val storiesState: StateFlow<Result<List<ListStoryItem>>> = _storiesState

    fun fetchStories() {
        _storiesState.value = Result.Loading

        viewModelScope.launch {
            val result = listStoryUseCase.execute()
            _storiesState.value = result
        }
    }
}