package com.inseong.composechart.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 터치 시 표시되는 툴팁 버블의 스타일 설정.
 *
 * @param backgroundColor 툴팁 배경색 (다크 계열 권장)
 * @param textColor 툴팁 텍스트 색상
 * @param textSize 툴팁 텍스트 크기
 * @param cornerRadius 툴팁 모서리 둥글기
 * @param paddingHorizontal 툴팁 내부 가로 패딩
 * @param paddingVertical 툴팁 내부 세로 패딩
 * @param indicatorRadius 데이터 포인트 인디케이터(원형 도트)의 반지름
 * @param indicatorColor 인디케이터 내부 색상
 * @param indicatorBorderColor 인디케이터 테두리 색상. [Color.Unspecified]이면 라인 색상 사용
 * @param indicatorBorderWidth 인디케이터 테두리 두께
 */
data class TooltipStyle(
    val backgroundColor: Color = Color(0xFF333333),
    val textColor: Color = Color.White,
    val textSize: TextUnit = 12.sp,
    val cornerRadius: Dp = 8.dp,
    val paddingHorizontal: Dp = 12.dp,
    val paddingVertical: Dp = 6.dp,
    val indicatorRadius: Dp = 5.dp,
    val indicatorColor: Color = Color.White,
    val indicatorBorderColor: Color = Color.Unspecified,
    val indicatorBorderWidth: Dp = 2.dp,
)
