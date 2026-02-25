package com.inseong.composechart.internal.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill

/**
 * Converts a list of data points into a smooth bezier curve [Path].
 *
 * Uses Catmull-Rom spline to cubic bezier conversion,
 * with tension controlling the curve smoothness.
 *
 * @param tension Curve tension (0.0 = nearly straight, 1.0 = very smooth). Default: 0.3
 * @return [Path] with bezier curves applied
 */
internal fun List<Offset>.toBezierPath(tension: Float = 0.3f): Path {
    val path = Path()
    if (isEmpty()) return path

    path.moveTo(first().x, first().y)

    if (size == 1) return path
    if (size == 2) {
        path.lineTo(last().x, last().y)
        return path
    }

    for (i in 0 until size - 1) {
        val p0 = if (i > 0) this[i - 1] else this[i]
        val p1 = this[i]
        val p2 = this[i + 1]
        val p3 = if (i < size - 2) this[i + 2] else this[i + 1]

        // Control point calculation (Catmull-Rom -> Cubic Bezier)
        val cp1x = p1.x + (p2.x - p0.x) * tension
        val cp1y = p1.y + (p2.y - p0.y) * tension
        val cp2x = p2.x - (p3.x - p1.x) * tension
        val cp2y = p2.y - (p3.y - p1.y) * tension

        path.cubicTo(cp1x, cp1y, cp2x, cp2y, p2.x, p2.y)
    }

    return path
}

/**
 * Creates a [Path] connecting data points with straight lines.
 *
 * @return [Path] with linear connections
 */
internal fun List<Offset>.toLinearPath(): Path {
    val path = Path()
    if (isEmpty()) return path

    path.moveTo(first().x, first().y)
    for (i in 1 until size) {
        path.lineTo(this[i].x, this[i].y)
    }
    return path
}

/**
 * Fills the area below a line [Path] with a gradient.
 *
 * Clones the line path, then creates a closed area down to the bottom boundary,
 * filling it with a top-to-bottom gradient that fades to transparent.
 *
 * @param linePath Line path (unclosed)
 * @param color Gradient start color (top)
 * @param alpha Gradient start opacity
 * @param bottomY Bottom Y coordinate of the fill area (chart area bottom)
 * @param startX Start X coordinate of the line
 * @param endX End X coordinate of the line
 */
internal fun DrawScope.drawGradientFill(
    linePath: Path,
    color: Color,
    alpha: Float,
    bottomY: Float,
    startX: Float,
    endX: Float,
) {
    val fillPath = Path().apply {
        addPath(linePath)
        // From line end, go down to bottom, back to start bottom, then close
        lineTo(endX, bottomY)
        lineTo(startX, bottomY)
        close()
    }

    drawPath(
        path = fillPath,
        brush = Brush.verticalGradient(
            colors = listOf(
                color.copy(alpha = alpha),
                Color.Transparent,
            ),
        ),
        style = Fill,
    )
}
