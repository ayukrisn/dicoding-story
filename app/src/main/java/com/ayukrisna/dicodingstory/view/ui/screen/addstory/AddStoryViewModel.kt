package com.ayukrisna.dicodingstory.view.ui.screen.addstory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ayukrisna.dicodingstory.data.remote.response.AddStoryResponse
import com.ayukrisna.dicodingstory.domain.usecase.ValidateStoryUseCase
import com.ayukrisna.dicodingstory.util.Result

class AddStoryViewModel : ViewModel() {
    private val validateStoryUseCase = ValidateStoryUseCase()
    var draftState by mutableStateOf(AddStoryState())

    private val _addStoryState = MutableLiveData<Result<AddStoryResponse>>(Result.Idle)
    val addStoryState: LiveData<Result<AddStoryResponse>> = _addStoryState

    fun onEvent(event: AddStoryEvent) {
        when (event) {
            is AddStoryEvent.StoryChanged -> {
                draftState = draftState.copy(storyDraft = event.storyDraft)
                validateStory()
            }
            is AddStoryEvent.UriChanged -> {
                draftState = draftState.copy(uriPicture = event.uriPicture)
                validateUri()
            }
            is AddStoryEvent.Submit -> {
                val isStoryValid = validateStory()
                val isUriValid = validateUri()

                if (isStoryValid && isUriValid)
                addStory()
            }
        }
    }

    private fun validateStory(): Boolean {
        val storyResult = validateStoryUseCase.execute(draftState.storyDraft)
        draftState = draftState.copy(storyError = storyResult.errorMessage)
        return storyResult.successful
    }

    private fun validateUri(): Boolean {
        val uriResult = validateStoryUseCase.execute(draftState.uriPicture)
        draftState = draftState.copy(storyError = uriResult.errorMessage)
        return uriResult.successful
    }

    private fun addStory() {

    }

}