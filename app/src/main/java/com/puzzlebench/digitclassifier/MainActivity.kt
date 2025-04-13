package com.puzzlebench.digitclassifier

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.puzzlebench.digitclassifier.ui.feature.ClassifierScreen
import com.puzzlebench.digitclassifier.ui.feature.viewModel.ClassifierUiAction
import com.puzzlebench.digitclassifier.ui.feature.viewModel.ClassifierViewModel
import com.puzzlebench.digitclassifier.ui.theme.DigitClassifierTheme
import kotlin.getValue

const val DEFAULT_VALUE_PREDICTED_NUMBER = -1

class MainActivity : ComponentActivity() {

    private val classifierViewModel: ClassifierViewModel by viewModels {
        ClassifierViewModel.getFactory(
            this
        )
    }

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        classifierViewModel.initializeClassifier()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val uiState by classifierViewModel.classifierUiState.collectAsState()
            val uiAction = classifierViewModel::onUiAction

            DigitClassifierTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    ClassifierScreen(
                        modifier = Modifier.padding(innerPadding),
                        uiState = uiState,
                        restartButtonClicked = { uiAction(ClassifierUiAction.OnRestart) },
                        identifyButtonClicked = { bitmap ->
                            uiAction(
                                ClassifierUiAction.OnIdentifyBitmap(
                                    bitmap
                                )
                            )
                        },
                        dismissButtonClicked = { uiAction(ClassifierUiAction.OnCloseErrorDialog) },
                        onDragEnd = { uiAction(ClassifierUiAction.OnReadyToIdentifyNumber) },
                        onClassifyImage = { bitmap ->
                            classifierViewModel.classifyImage(bitmap)
                        }
                    )
                }
            }
        }
    }
}
