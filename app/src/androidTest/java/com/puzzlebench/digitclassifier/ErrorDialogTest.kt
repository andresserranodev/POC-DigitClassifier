package com.puzzlebench.digitclassifier
import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.puzzlebench.digitclassifier.ui.compose.ErrorDialog
import com.puzzlebench.digitclassifier.ui.theme.DigitClassifierTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ErrorDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun errorDialog_isDisplayed(): Unit = with(composeTestRule) {
        setContent {
            DigitClassifierTheme {
                ErrorDialog(
                    visibility = true,
                    onDismissRequest = {},
                    reDrawButtonClick = {}
                )
            }
        }

        onNodeWithText(context.getString(R.string.prediction_error_message))
            .assertExists()
    }

    @Test
    fun errorDialog_closeButtonClick(): Unit = with(composeTestRule) {
        var isDismissed = false
        setContent {
            DigitClassifierTheme {
                ErrorDialog(
                    visibility = true,
                    onDismissRequest = { isDismissed = true },
                    reDrawButtonClick = {}
                )
            }
        }

        onNodeWithText(context.getString(R.string.dialog_close_button))
            .performClick()

        assert(isDismissed)
    }

    @Test
    fun errorDialog_reDrawButtonClick(): Unit = with(composeTestRule) {
        var isRedrawClicked = false
        setContent {
            DigitClassifierTheme {
                ErrorDialog(
                    visibility = true,
                    onDismissRequest = {},
                    reDrawButtonClick = { isRedrawClicked = true }
                )
            }
        }
        onNodeWithText(context.getString(R.string.dialog_redraw_button))
            .performClick()
        assert(isRedrawClicked)
    }
}