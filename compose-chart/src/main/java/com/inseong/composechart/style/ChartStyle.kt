package com.inseong.composechart.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 모든 차트에 공통으로 적용되는 기본 스타일.
 *
 * @param backgroundColor 차트 영역의 배경색
 * @param chartPadding 차트 컨텐츠 영역의 내부 패딩
 */
data class ChartStyle(
    val backgroundColor: Color = Color.Transparent,
    val chartPadding: Dp = 16.dp,
)
