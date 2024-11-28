package com.ayukrisna.dicodingstory.view.ui.screen.addstory

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayukrisna.dicodingstory.data.remote.response.AddStoryResponse
import com.ayukrisna.dicodingstory.domain.usecase.AddStoryUseCase
import com.ayukrisna.dicodingstory.domain.usecase.ValidateStoryUseCase
import com.ayukrisna.dicodingstory.util.FileHelper
import com.ayukrisna.dicodingstory.util.Result
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryViewModel(
    private val addStoryUseCase: AddStoryUseCase,
    private val fileHelper: FileHelper
) : ViewModel() {
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
                addStory(draftState.storyDraft, draftState.uriPicture)
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

    private fun addStory(description: String, photoUri: Uri, lat: Float? = null, lon: Float? = null) {
        viewModelScope.launch {
            _addStoryState.value = Result.Loading
            val imageFile = fileHelper.uriToFile(photoUri)
            val reducedImageFile = fileHelper.reduceFileImage(imageFile)
            val requestImageFile = reducedImageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            val result = addStoryUseCase.execute(description, multipartBody, lat, lon)
            _addStoryState.value = result
        }
    }
}