package com.puzzlebench.digitclassifier.ui.feature.model

import androidx.compose.runtime.Immutable

@Immutable
data class ClassifierResult(
    val number: Int,
    val confidence: Float
)
