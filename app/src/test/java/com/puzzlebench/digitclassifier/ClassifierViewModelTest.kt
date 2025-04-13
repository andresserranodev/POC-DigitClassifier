package com.puzzlebench.digitclassifier

import com.puzzlebench.digitclassifier.tensorflowlite.DigitClassifierFacade
import com.puzzlebench.digitclassifier.ui.feature.viewModel.ClassifierUiAction
import com.puzzlebench.digitclassifier.ui.feature.viewModel.ClassifierUiState
import com.puzzlebench.digitclassifier.ui.feature.viewModel.ClassifierViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ClassifierViewModelTest {

    private lateinit var viewModel: ClassifierViewModel

    @Before
    fun setup() {
        val digitClassifierFacade = mockk<DigitClassifierFacade>()
        viewModel = ClassifierViewModel(digitClassifierFacade)
    }

    @Test
    fun validatePredictedNumber_GivenConfidenceLessThanOne_WhenCalled_ThenShowErrorDialog() = runTest {
        // Given a number and a low confidence value
        val number = 5
        val confidence = 0.5f

        // When validatePredictedNumber is called
        viewModel.validatePredictedNumber(number, confidence)

        // Then the state should be ShowErrorDialog
        val state = viewModel.classifierUiState.first()
        assertEquals(ClassifierUiState.ShowErrorDialog, state)
    }

    @Test
    fun validatePredictedNumber_GivenConfidenceGreaterOrEqualOne_WhenCalled_ThenSuccess() = runTest {
        // Given a number and a sufficient confidence value
        val number = 7
        val confidence = 1.5f

        // When validatePredictedNumber is called
        viewModel.validatePredictedNumber(number, confidence)

        // Then the state should be Success with the given number and confidence
        val state = viewModel.classifierUiState.first()
        when (state) {
            is ClassifierUiState.Success -> {
                assertEquals(number, state.classifierResult.number)
                assertEquals(confidence, state.classifierResult.confidence)
            }
            else -> throw AssertionError("Expected Success state")
        }
    }

    @Test
    fun onUiAction_GivenOnRestart_WhenCalled_ThenDefaultState() = runTest {
        // Given the current state is not default (simulate by setting a different state)
        viewModel.validatePredictedNumber(5, 1.5f) // Success state

        // When onUiAction is called with OnRestart
        viewModel.onUiAction(ClassifierUiAction.OnRestart)

        // Then the state should be Default
        val state = viewModel.classifierUiState.first()
        assertEquals(ClassifierUiState.Default, state)
    }

    @Test
    fun onUiAction_GivenOnReadyToIdentifyNumber_WhenCalled_ThenReadyToIdentify() = runTest {
        // Given no preconditions required

        // When onUiAction is called with OnReadyToIdentifyNumber
        viewModel.onUiAction(ClassifierUiAction.OnReadyToIdentifyNumber)

        // Then the state should be ReadyToIdentify
        val state = viewModel.classifierUiState.first()
        assertEquals(ClassifierUiState.ReadyToIdentify, state)
    }

    @Test
    fun onUiAction_GivenOnCloseErrorDialog_WhenCalled_ThenHideErrorDialog() = runTest {
        // Given the error dialog state is shown
        viewModel.validatePredictedNumber(5, 0.5f)

        // When onUiAction is called with OnCloseErrorDialog
        viewModel.onUiAction(ClassifierUiAction.OnCloseErrorDialog)

        // Then the state should be HideErrorDialog
        val state = viewModel.classifierUiState.first()
        assertEquals(ClassifierUiState.HideErrorDialog, state)
    }

    @Test
    fun onUiAction_GivenOnReDraw_WhenCalled_ThenDefaultState() = runTest {
        // Given the current state is not default (simulate by setting a different state)
        viewModel.validatePredictedNumber(5, 1.5f) // Success state

        // When onUiAction is called with OnReDraw
        viewModel.onUiAction(ClassifierUiAction.OnReDraw)

        // Then the state should be Default
        val state = viewModel.classifierUiState.first()
        assertEquals(ClassifierUiState.Default, state)
    }
}
