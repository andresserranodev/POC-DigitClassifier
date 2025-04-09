package com.puzzlebench.digitclassifier.ui.feature.viewModel

import androidx.lifecycle.ViewModel
import com.puzzlebench.digitclassifier.ui.feature.model.ClassifierResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClassifierViewModel : ViewModel() {

    private val _classifierUiState = MutableStateFlow<ClassifierUiState>(ClassifierUiState.Default)
    val classifierUiState = _classifierUiState.asStateFlow()

    fun validatePredictedNumber(number: Int, confidence: Float) {
        _classifierUiState.value = if (confidence < 1) {
            ClassifierUiState.ShowErrorDialog
        } else {
            ClassifierUiState.Success(ClassifierResult(number, confidence))
        }
    }

    fun onUiAction(action: ClassifierUiAction) {
        _classifierUiState.value = when (action) {
            is ClassifierUiAction.OnReadyToIdentifyNumber -> ClassifierUiState.ReadyToIdentify
            is ClassifierUiAction.OnRestart, is ClassifierUiAction.OnReDraw -> ClassifierUiState.Default
            is ClassifierUiAction.OnCloseErrorDialog -> ClassifierUiState.HideErrorDialog
            is ClassifierUiAction.OnIdentifyBitmap -> ClassifierUiState.IdentifyImage(bitmap = action.bitmap)
        }
    }
}
