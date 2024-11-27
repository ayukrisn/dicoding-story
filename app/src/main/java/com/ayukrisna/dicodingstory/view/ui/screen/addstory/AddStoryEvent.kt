package com.ayukrisna.dicodingstory.view.ui.screen.addstory

import android.net.Uri
import com.ayukrisna.dicodingstory.view.ui.screen.login.LoginEvent

sealed class AddStoryEvent {
    data class StoryChanged(val storyDraft: String) : AddStoryEvent()
    data class UriChanged(val uriPicture: Uri): AddStoryEvent()
    object Submit: AddStoryEvent()
}