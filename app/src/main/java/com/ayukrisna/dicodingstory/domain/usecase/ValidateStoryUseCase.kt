package com.ayukrisna.dicodingstory.domain.usecase

import android.net.Uri
import com.ayukrisna.dicodingstory.R
import com.ayukrisna.dicodingstory.domain.model.ValidationResult
import com.ayukrisna.dicodingstory.util.UiText

class ValidateStoryUseCase: BaseUseCase<Any, ValidationResult>() {
    override fun execute(input: Any): ValidationResult {
        when (input) {
            is String -> {
                if (input.isBlank()) {
                    return ValidationResult(
                        successful = false,
                        errorMessage = UiText.StringResource(resId = R.string.storyCannotBeBlank)
                    )
                }
            }
            is Uri -> {
                if (input == Uri.EMPTY) {
                    return ValidationResult(
                        successful = false,
                        errorMessage = UiText.StringResource(resId = R.string.storyCannotBeBlank)
                    )
                }
            }
        }

        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}