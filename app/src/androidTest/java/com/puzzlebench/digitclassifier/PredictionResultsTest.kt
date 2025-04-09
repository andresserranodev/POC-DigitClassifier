package com.puzzlebench.digitclassifier

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.puzzlebench.digitclassifier.ui.compose.PredictionResults
import com.puzzlebench.digitclassifier.ui.theme.DigitClassifierTheme
import kotlinx.coroutines.delay
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PredictionResultsTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    val context: Context = InstrumentationRegistry.getInstrumentation().targetContext


    @Test
    fun testPredictionResultsDisplayed(): Unit = with(composeTestRule) {
        setContent {
            DigitClassifierTheme {
                PredictionResults(predictedNumber = 7, confidence = 0.85F)
            }
        }

        onNodeWithText(context.getString(R.string.classification_success_message)).assertExists()
        onNodeWithText("7").assertExists()
    }
}