package com.puzzlebench.digitclassifier.ui.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DrawingBoard(
    modifier: Modifier,
    onDragEnd: () -> Unit,
    linesState: MutableList<Line>
) {
    Canvas(
        modifier = modifier
            .size(width = 400.dp, height = 400.dp)
            .background(Color.Black)
            .pointerInput(true) {
                detectDragGestures(onDrag = { change, dragAmount ->
                    change.consume()

                    val line = Line(
                        start = change.position - dragAmount,
                        end = change.position
                    )

                    linesState.add(line)
                }, onDragEnd = {
                        onDragEnd()
                    })
            }
    ) {
        linesState.forEach { line ->
            drawLine(
                color = line.color,
                start = line.start,
                end = line.end,
                strokeWidth = line.strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

@Stable
data class Line(
    val start: Offset,
    val end: Offset,
    val color: Color = Color.White,
    val strokeWidth: Dp = 30.dp
)
