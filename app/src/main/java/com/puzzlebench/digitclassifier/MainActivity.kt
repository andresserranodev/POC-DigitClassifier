package com.puzzlebench.digitclassifier

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.graphics.ImageBitmap
import com.google.android.gms.tasks.Task
import com.google.android.gms.tflite.java.TfLite
import com.puzzlebench.digitclassifier.tensorflowlite.DigitClassifier
import com.puzzlebench.digitclassifier.tensorflowlite.DigitClassifier.Companion.TAG
import com.puzzlebench.digitclassifier.ui.feature.ClassifierScreen
import com.puzzlebench.digitclassifier.ui.feature.viewModel.ClassifierUiAction
import com.puzzlebench.digitclassifier.ui.feature.viewModel.ClassifierViewModel
import com.puzzlebench.digitclassifier.ui.theme.DigitClassifierTheme
import kotlin.getValue

const val DEFAULT_VALUE_PREDICTED_NUMBER = -1

class MainActivity : ComponentActivity() {

    private val classifierViewModel: ClassifierViewModel by viewModels()

    private var digitClassifier = DigitClassifier(this)

    private val initializeTasks: Task<Void> by lazy {
        // Initialize the TensorFlow Lite interpreter here
        TfLite.initialize(this).addOnFailureListener { error ->
            Log.e(TAG, "Error initializing TensorFlow Lite.", error)
        }
    }

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        initializeTasks.addOnSuccessListener {
            Log.d(TAG, "TensorFlow Lite initialized successfully.")
            // Setup digit classifier.
            digitClassifier.initialize().addOnFailureListener { e ->
                Log.e(
                    TAG,
                    "Error to setting up digit classifier.",
                    e
                )
            }
        }
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
                        onClassifyImage = { bitmap -> classifyDrawing(bitmap) }
                    )
                }
            }
        }
    }

    private fun classifyDrawing(bitmap: ImageBitmap) {
        if (digitClassifier.isInitialized) {
            digitClassifier
                .classifyAsync(bitmap)
                .addOnSuccessListener { resultText ->
                    classifierViewModel.validatePredictedNumber(resultText.first, resultText.second)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error classifying drawing.", e)
                }
        }
    }

    override fun onDestroy() {
        // Sync DigitClassifier instance lifecycle with MainActivity lifecycle,
        // and free up resources (e.g. TF Lite instance) once the activity is destroyed.
        digitClassifier.close()
        super.onDestroy()
    }
}
