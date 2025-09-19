package com.example.streamchatdemo

import androidx.compose.runtime.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min

@Composable
fun CanvasScreen() {
    // ---- layout / sizes -----------------------------------------------------
    var boxSize by remember { mutableStateOf(IntSize.Zero) } // px
    val density = LocalDensity.current
    val strokePx = with(density) { 3.dp.toPx() }
    val handleRadiusPx = with(density) { 12.dp.toPx() }
    val handleGrabRadiusPx = with(density) { 24.dp.toPx() }
    val minRadiusPx = with(density) { 24.dp.toPx() }

    // ---- circle state (px) --------------------------------------------------
    var center by rememberSaveable(stateSaver = OffsetSaver) { mutableStateOf(Offset(300f, 300f)) }
    var radius by rememberSaveable { mutableStateOf(150f) }
    var isResizing by remember { mutableStateOf(false) }

    // ---- UI -----------------------------------------------------------------
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .onSizeChanged { size ->
                    boxSize = size
                    // Clamp center when size is known
                    center = clampCenter(center, radius, size)
                    // Clamp radius too (in case view got smaller)
                    radius = clampRadius(radius, center, size, minRadiusPx)
                }
        ) {
            // Background image
            Image(
                painter = painterResource(id = R.drawable.sample),
                contentDescription = "Sample",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            // Overlay canvas (hit area = whole box)
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(boxSize) {      // or .pointerInput(Unit)
                        detectDragGestures(
                            onDragStart = { down ->
                                val handleCenter = Offset(center.x + radius, center.y)
                                val distToHandle = distance(down, handleCenter)
                                isResizing = distToHandle <= handleGrabRadiusPx
                                if (!isResizing && distance(down, center) > radius) {
                                    isResizing = false
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                if (boxSize == IntSize.Zero) return@detectDragGestures

                                if (isResizing) {
                                    val newR = distance(center, change.position)
                                    radius = clampRadius(newR, center, boxSize, minRadiusPx)
                                } else {
                                    val newCenter = Offset(center.x + dragAmount.x, center.y + dragAmount.y)
                                    center = clampCenter(newCenter, radius, boxSize)
                                }
                            },
                            onDragEnd = { isResizing = false },
                            onDragCancel = { isResizing = false }
                        )
                    }

            ) {
                // Fill (subtle)
                drawCircle(
                    color = Color.Cyan.copy(alpha = 0.15f),
                    radius = radius,
                    center = center
                )
                // Stroke (dashed)
                drawCircle(
                    color = Color.Cyan,
                    radius = radius,
                    center = center,
                    style = Stroke(
                        width = strokePx,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 12f))
                    )
                )
                // Handle (rightmost point on the circle)
                val handleCenter = Offset(center.x + radius, center.y)
                drawCircle(
                    color = Color.Cyan,
                    radius = handleRadiusPx,
                    center = handleCenter
                )
            }
        }

        Text(
            text = "x=${center.x.toInt()}, y=${center.y.toInt()}, r=${radius.toInt()}",
            textAlign = TextAlign.Center
        )
    }
}

/* ----------------------------- helpers ----------------------------- */

private fun distance(a: Offset, b: Offset): Float = hypot((a.x - b.x), (a.y - b.y))

private fun clampCenter(center: Offset, r: Float, size: IntSize): Offset {
    if (size == IntSize.Zero) return center
    val minX = r
    val maxX = size.width.toFloat() - r
    val minY = r
    val maxY = size.height.toFloat() - r
    val cx = center.x.coerceIn(minX, maxX)
    val cy = center.y.coerceIn(minY, maxY)
    return Offset(cx, cy)
}

private fun clampRadius(r: Float, center: Offset, size: IntSize, minR: Float): Float {
    if (size == IntSize.Zero) return r
    val maxR = min(
        min(center.x, size.width - center.x),
        min(center.y, size.height - center.y)
    )
    return r.coerceIn(minR, maxR)
}

// Saver for Offset so rememberSaveable can persist it (x,y)
private val OffsetSaver: Saver<Offset, out Any> = listSaver(
    save = { listOf(it.x, it.y) },
    restore = { list -> Offset(list[0], list[1]) }
)

