package com.puzzlebench.digitclassifier.tensorflowlite

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import com.google.android.gms.tasks.Task
import com.google.android.gms.tflite.java.TfLite
import com.puzzlebench.digitclassifier.tensorflowlite.DigitClassifier.Companion.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DigitClassifierFacade(context: Context) {

    private val digitClassifier = DigitClassifier(context)

    private val initializeTasks: Task<Void> by lazy {
        // Initialize the TensorFlow Lite interpreter here
        TfLite.initialize(context).addOnFailureListener { error ->
            Log.e(TAG, "Error initializing TensorFlow Lite.", error)
        }
    }

    suspend fun initialize() = withContext(Dispatchers.IO) {
        initializeTasks.addOnSuccessListener {
            Log.d(TAG, "TensorFlow Lite initialized successfully.")
            // Setup digit classifier.
            digitClassifier.initialize().addOnFailureListener { digitClassifierError ->
                Log.e(
                    TAG,
                    "Error to setting up digit classifier.",
                    digitClassifierError
                )
            }
        }
    }

    suspend fun classify(bitmap: ImageBitmap): Pair<Int, Float> = withContext(Dispatchers.IO) {
        digitClassifier.classify(bitmap)
    }

    fun close() {
        // free up resources (e.g. TF Lite instance) once is not need it anymore.
        digitClassifier.close()
    }
}
