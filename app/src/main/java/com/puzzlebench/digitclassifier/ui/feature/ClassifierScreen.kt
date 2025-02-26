package com.puzzlebench.digitclassifier.ui.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.puzzlebench.digitclassifier.DEFAULT_VALUE_PREDICTED_NUMBER
import com.puzzlebench.digitclassifier.R
import com.puzzlebench.digitclassifier.compose.DrawingBoard
import com.puzzlebench.digitclassifier.compose.ErrorDialog
import com.puzzlebench.digitclassifier.compose.Line
import com.puzzlebench.digitclassifier.compose.PredictionResults
import com.puzzlebench.digitclassifier.compose.TitleMessage
import com.puzzlebench.digitclassifier.ui.feature.viewModel.ClassifierUiState
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class)
@Composable
fun ClassifierScreen(
    modifier: Modifier,
    uiState: ClassifierUiState,
    restartButtonClicked: () -> Unit,
    identifyButtonClicked: (ImageBitmap) -> Unit,
    dismissButtonClicked: () -> Unit,
    onDragEnd: () -> Unit,
    onClassifyImage: (ImageBitmap) -> Unit,
) {
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


    when (uiState) {
        is ClassifierUiState.Success -> {
            confidenceResultState =
                uiState.classifierResult.confidence
            predictionResultState =
                uiState.classifierResult.number
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
            onClassifyImage(uiState.bitmap)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleMessage(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
        )
        DrawingBoard(
            modifier = Modifier.capturable(captureController),
            onDragEnd = { onDragEnd() },
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
                onClick = { restartButtonClicked() }) {
                Text(stringResource(R.string.clear_button_text))
            }
            if (isClassifyButtonVisible) {
                Button(
                    onClick = {
                        scope.launch {
                            identifyButtonClicked(captureController.captureAsync().await())
                        }
                    }) {
                    Text(stringResource(R.string.identify_button_text))
                }
            }
        }


        PredictionResults(
            predictedNumber = predictionResultState,
            confidence = confidenceResultState
        )
        ErrorDialog(
            openAlertDialog,
            onDismissRequest = { dismissButtonClicked() },
            reDrawButtonClick = { restartButtonClicked() })
    }
}