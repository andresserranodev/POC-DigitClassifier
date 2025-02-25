package com.puzzlebench.digitclassifier

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.google.android.gms.tasks.Task
import com.google.android.gms.tflite.java.TfLite
import com.puzzlebench.digitclassifier.tensorflowlite.DigitClassifier.Companion.TAG
import com.puzzlebench.digitclassifier.compose.DrawingBoard
import com.puzzlebench.digitclassifier.compose.ErrorDialog
import com.puzzlebench.digitclassifier.compose.Line
import com.puzzlebench.digitclassifier.compose.PredictionResults
import com.puzzlebench.digitclassifier.compose.TitleMessage
import com.puzzlebench.digitclassifier.tensorflowlite.DigitClassifier
import com.puzzlebench.digitclassifier.ui.feature.viewModel.ClassifierUiAction
import com.puzzlebench.digitclassifier.ui.feature.viewModel.ClassifierUiState
import com.puzzlebench.digitclassifier.ui.feature.viewModel.ClassifierViewModel
import com.puzzlebench.digitclassifier.ui.theme.DigitClassifierTheme
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch
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
            val captureController = rememberCaptureController()
            val scope = rememberCoroutineScope()

            val drewLinesState = remember { mutableStateListOf<Line>() }
            var predictionResultState by rememberSaveable {
                mutableIntStateOf(
                    DEFAULT_VALUE_PREDICTED_NUMBER
                )
            }
            var confidenceResultState: Float by rememberSaveable { mutableFloatStateOf(-1f) }
            var isClassifyButtonVisible by rememberSaveable { mutableStateOf(false) }
            var openAlertDialog by rememberSaveable { mutableStateOf(false) }

            val uiState by classifierViewModel.classifierUiState.collectAsState()
            val uiAction = classifierViewModel::onUiAction

            when (uiState) {
                is ClassifierUiState.Success -> {
                    confidenceResultState =
                        (uiState as ClassifierUiState.Success).classifierResult.confidence
                    predictionResultState =
                        (uiState as ClassifierUiState.Success).classifierResult.number
                }

                is ClassifierUiState.ShowErrorDialog -> {
                    openAlertDialog = true
                }

                is ClassifierUiState.Default -> {
                    drewLinesState.clear()
                    predictionResultState = DEFAULT_VALUE_PREDICTED_NUMBER
                    confidenceResultState = -1f
                    isClassifyButtonVisible = false
                    openAlertDialog = false
                }

                is ClassifierUiState.ReadyToIdentify -> {
                    isClassifyButtonVisible = true
                }

                ClassifierUiState.HideErrorDialog -> {
                    openAlertDialog = false
                }

                is ClassifierUiState.IdentifyImage ->{
                    classifyDrawing((uiState as ClassifierUiState.IdentifyImage).bitmap)
                }
            }

            DigitClassifierTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(
                        modifier = Modifier.padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TitleMessage(
                            modifier = Modifier
                                .padding(innerPadding)
                                .align(Alignment.CenterHorizontally)
                        )
                        DrawingBoard(
                            modifier = Modifier.capturable(captureController),
                            onDragEnd = { uiAction(ClassifierUiAction.OnReadyToIdentifyNumber) },
                            linesState = drewLinesState
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterHorizontally
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp)
                        ) {
                            Button(
                                onClick = { uiAction(ClassifierUiAction.OnRestart) }) {
                                Text(getString(R.string.clear_button_text))
                            }
                            if (isClassifyButtonVisible) {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            uiAction(ClassifierUiAction.OnIdentifyBitmap(captureController.captureAsync().await()))
                                        }
                                    }) {
                                    Text(getString(R.string.identify_button_text))
                                }
                            }
                        }


                        PredictionResults(
                            predictedNumber = predictionResultState,
                            confidence = confidenceResultState
                        )
                        ErrorDialog(
                            openAlertDialog,
                            onDismissRequest = { uiAction(ClassifierUiAction.OnCloseErrorDialog) },
                            reDrawButtonClick = { uiAction(ClassifierUiAction.OnRestart) })
                    }
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