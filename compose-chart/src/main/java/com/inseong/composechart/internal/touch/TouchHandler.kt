package com.inseong.composechart.internal.touch

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs

/**
 * 차트 터치 인터랙션을 처리하는 Modifier 확장.
 *
 * 탭과 드래그(스크러빙) 제스처를 감지하여 터치 위치를 콜백으로 전달한다.
 * 터치 종료 시 null을 전달한다.
 *
 * @param onTouch 터치 위치 [Offset] 또는 터치 종료 시 null
 */
internal fun Modifier.chartTouchHandler(
    onTouch: (Offset?) -> Unit,
): Modifier = this
    .pointerInput(Unit) {
        detectTapGestures(
            onPress = { offset ->
                onTouch(offset)
                // 손가락을 떼면 터치 해제
                tryAwaitRelease()
                onTouch(null)
            },
        )
    }
    .pointerInput(Unit) {
        detectDragGestures(
            onDragStart = { offset -> onTouch(offset) },
            onDrag = { change, _ ->
                change.consume()
                onTouch(change.position)
            },
            onDragEnd = { onTouch(null) },
            onDragCancel = { onTouch(null) },
        )
    }

/**
 * 터치 X 좌표에서 가장 가까운 데이터 포인트의 인덱스를 찾는다.
 *
 * @param touchX 터치한 X 좌표 (픽셀)
 * @param pointXPositions 각 데이터 포인트의 X 좌표 목록 (픽셀)
 * @return 가장 가까운 포인트의 인덱스. 목록이 비어있으면 -1
 */
internal fun findNearestPointIndex(touchX: Float, pointXPositions: List<Float>): Int {
    if (pointXPositions.isEmpty()) return -1
    var minDistance = Float.MAX_VALUE
    var nearestIndex = 0
    pointXPositions.forEachIndexed { index, x ->
        val distance = abs(touchX - x)
        if (distance < minDistance) {
            minDistance = distance
            nearestIndex = index
        }
    }
    return nearestIndex
}
