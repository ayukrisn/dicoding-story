package com.ayukrisna.dicodingstory.view.ui.screen.story.addstory

import android.net.Uri
import com.ayukrisna.dicodingstory.util.UiText

data class AddStoryState (
    val uriPicture: Uri = Uri.EMPTY,
    val uriError: UiText? = null,
    val storyDraft: String = "",
    val storyError: UiText? = null,
)