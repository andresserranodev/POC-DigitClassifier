package com.puzzlebench.digitclassifier.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzlebench.digitclassifier.R

@Composable
fun TitleMessage(modifier: Modifier) {
    Text(
        text = stringResource(R.string.prediction_text_placeholder),
        modifier = modifier.padding(16.dp),
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTitleMessage() {
    TitleMessage(Modifier)
}
