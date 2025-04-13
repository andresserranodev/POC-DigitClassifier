package com.puzzlebench.digitclassifier.ui.feature.viewModel

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.puzzlebench.digitclassifier.tensorflowlite.DigitClassifierFacade
import com.puzzlebench.digitclassifier.ui.feature.model.ClassifierResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClassifierViewModel(private val digitClassifierFacade: DigitClassifierFacade) : ViewModel() {

    companion object {
        // TODO move to DI
        fun getFactory(context: Context) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val digitClassificationHelper = DigitClassifierFacade(context)
                return ClassifierViewModel(digitClassificationHelper) as T
            }
        }
    }

    private val _classifierUiState = MutableStateFlow<ClassifierUiState>(ClassifierUiState.Default)
    val classifierUiState = _classifierUiState.asStateFlow()

    fun initializeClassifier() {
        viewModelScope.launch {
            digitClassifierFacade.initialize()
        }
    }

    fun classifyImage(bitmap: ImageBitmap) {
        viewModelScope.launch {
            val result = digitClassifierFacade.classify(bitmap)
            validatePredictedNumber(result.first, result.second)
        }
    }

    fun validatePredictedNumber(number: Int, confidence: Float) {
        _classifierUiState.value = if (confidence < 1) {
            ClassifierUiState.ShowErrorDialog
        } else {
            ClassifierUiState.Success(ClassifierResult(number, confidence))
        }
    }

    fun onUiAction(action: ClassifierUiAction) {
        _classifierUiState.value = when (action) {
            is ClassifierUiAction.OnReadyToIdentifyNumber ->
                ClassifierUiState.ReadyToIdentify
            is ClassifierUiAction.OnRestart,
            is ClassifierUiAction.OnReDraw ->
                ClassifierUiState.Default
            is ClassifierUiAction.OnCloseErrorDialog ->
                ClassifierUiState.HideErrorDialog
            is ClassifierUiAction.OnIdentifyBitmap ->
                ClassifierUiState.IdentifyImage(bitmap = action.bitmap)
        }
    }

    override fun onCleared() {
        super.onCleared()
        digitClassifierFacade.close()
    }
}
