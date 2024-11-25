package com.ayukrisna.dicodingstory.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
	@SerializedName("error")
	val error: Boolean? = null,
	@SerializedName("message")
	val message: String? = null
)
