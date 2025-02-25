package com.puzzlebench.digitclassifier.ui.feature.viewModel

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.ImageBitmap
import com.puzzlebench.digitclassifier.ui.feature.model.ClassifierResult

@Stable
sealed interface ClassifierUiState {
    data class Success(val classifierResult: ClassifierResult) : ClassifierUiState
    data object ShowErrorDialog : ClassifierUiState
    data object Default : ClassifierUiState
    data object ReadyToIdentify : ClassifierUiState
    data object HideErrorDialog : ClassifierUiState
    data class IdentifyImage(val bitmap: ImageBitmap) : ClassifierUiState
}