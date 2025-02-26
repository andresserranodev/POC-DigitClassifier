package com.puzzlebench.digitclassifier.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzlebench.digitclassifier.DEFAULT_VALUE_PREDICTED_NUMBER
import com.puzzlebench.digitclassifier.R
import com.puzzlebench.digitclassifier.ui.theme.DigitClassifierTheme

@Composable
fun PredictionResults(predictedNumber: Int, confidence: Float) {
    if (predictedNumber != DEFAULT_VALUE_PREDICTED_NUMBER) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {

            Text(
                text = stringResource(R.string.classification_success_message),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = predictedNumber.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
            Text(
                text = stringResource(R.string.confidence_result_message, confidence),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PredictionResultsPreview() {
    DigitClassifierTheme {
        PredictionResults(predictedNumber = 7, confidence = 2F)
    }
}
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PredictionResultsPreviewDark() {
    DigitClassifierTheme {
        PredictionResults(predictedNumber = 6, confidence = 1F)
    }
}