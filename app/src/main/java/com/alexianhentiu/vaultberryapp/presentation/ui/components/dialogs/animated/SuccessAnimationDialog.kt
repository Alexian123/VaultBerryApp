package com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay

@Composable
fun SuccessAnimationDialog(
    modifier: Modifier = Modifier,
    checkmarkColor: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 8.dp,
    animationDurationMillis: Int = 800, // Duration for the checkmark to draw
    displayDurationMillis: Long = 2000, // Total time the screen is shown
    onTimeout: () -> Unit  = {}
) {
    val progress = remember { Animatable(0f) } // For drawing the checkmark path

    Dialog(
        onDismissRequest = {}
    ) {
        LaunchedEffect(key1 = Unit) {
            // Animate the checkmark drawing
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = animationDurationMillis)
            )
            // Wait for the specified display duration
            delay(displayDurationMillis - animationDurationMillis.toLong().coerceAtLeast(0L))
            onTimeout()
        }

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(120.dp)) {
                val path = Path().apply {
                    // Define the checkmark path (adjust coordinates as needed for your desired shape)
                    moveTo(size.width * 0.2f, size.height * 0.5f)
                    lineTo(size.width * 0.45f, size.height * 0.75f)
                    lineTo(size.width * 0.8f, size.height * 0.3f)
                }

                val pathMeasure = PathMeasure()
                pathMeasure.setPath(path, false)

                val segmentPath = Path()
                pathMeasure.getSegment(0f, pathMeasure.length * progress.value, segmentPath, true)

                drawPath(
                    path = segmentPath,
                    color = checkmarkColor,
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessAnimationDialogPreview() {
    SuccessAnimationDialog()
}