package com.puzzlebench.digitclassifier.ui.feature.viewModel

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.ImageBitmap

@Stable
sealed interface ClassifierUiAction {
    data object OnRestart : ClassifierUiAction
    data object OnReadyToIdentifyNumber : ClassifierUiAction
    data object OnCloseErrorDialog : ClassifierUiAction
    data object OnReDraw : ClassifierUiAction
    data class OnIdentifyBitmap(val bitmap: ImageBitmap) : ClassifierUiAction
}
