package com.inseong.composechart

import androidx.compose.ui.graphics.Color

/**
 * 차트 라이브러리의 기본 설정값을 제공하는 객체.
 *
 * 각 차트 Composable에서 별도 색상을 지정하지 않으면
 * 이 기본 팔레트가 사용된다.
 */
object ChartDefaults {

    /**
     * 토스 스타일 기본 색상 팔레트.
     *
     * 멀티 시리즈 차트에서 순서대로 할당되며,
     * 시리즈 수가 팔레트를 초과하면 처음부터 반복된다.
     */
    val colors = listOf(
        Color(0xFF3182F6), // 토스 블루
        Color(0xFF48BB78), // 그린
        Color(0xFFED8936), // 오렌지
        Color(0xFFE53E3E), // 레드
        Color(0xFF9F7AEA), // 퍼플
        Color(0xFF38B2AC), // 틸
    )
}
