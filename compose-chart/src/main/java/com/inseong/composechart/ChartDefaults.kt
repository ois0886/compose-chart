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
     * 기본 색상 팔레트.
     *
     * 멀티 시리즈 차트에서 순서대로 할당되며,
     * 시리즈 수가 팔레트를 초과하면 처음부터 반복된다.
     */
    val colors = listOf(
        Color(0xFF3182F6), // 블루
        Color(0xFF48BB78), // 그린
        Color(0xFFED8936), // 오렌지
        Color(0xFFE53E3E), // 레드
        Color(0xFF9F7AEA), // 퍼플
        Color(0xFF38B2AC), // 틸
    )

    // ── 라이트/다크 테마 기본 색상 ──

    /** 그리드 라인 색상 (라이트) */
    internal val gridLineColorLight = Color(0xFFEEEEEE)
    /** 그리드 라인 색상 (다크) */
    internal val gridLineColorDark = Color(0xFF333333)

    /** 축 라벨 색상 (라이트) */
    internal val axisLabelColorLight = Color(0xFF9E9E9E)
    /** 축 라벨 색상 (다크) */
    internal val axisLabelColorDark = Color(0xFFBBBBBB)

    /** 게이지 트랙 색상 (라이트) */
    internal val gaugeTrackColorLight = Color(0xFFF0F0F0)
    /** 게이지 트랙 색상 (다크) */
    internal val gaugeTrackColorDark = Color(0xFF2A2A2A)

    /** 게이지 중앙 텍스트 색상 (라이트) */
    internal val gaugeCenterTextColorLight = Color(0xFF191F28)
    /** 게이지 중앙 텍스트 색상 (다크) */
    internal val gaugeCenterTextColorDark = Color(0xFFE8E8E8)

    /**
     * [Color.Unspecified]인 경우 다크 테마 여부에 따라 적절한 기본 색상을 반환한다.
     */
    internal fun resolveGridLineColor(color: Color, isDark: Boolean): Color {
        return if (color == Color.Unspecified) {
            if (isDark) gridLineColorDark else gridLineColorLight
        } else {
            color
        }
    }

    internal fun resolveAxisLabelColor(color: Color, isDark: Boolean): Color {
        return if (color == Color.Unspecified) {
            if (isDark) axisLabelColorDark else axisLabelColorLight
        } else {
            color
        }
    }

    internal fun resolveGaugeTrackColor(color: Color, isDark: Boolean): Color {
        return if (color == Color.Unspecified) {
            if (isDark) gaugeTrackColorDark else gaugeTrackColorLight
        } else {
            color
        }
    }

    internal fun resolveGaugeCenterTextColor(color: Color, isDark: Boolean): Color {
        return if (color == Color.Unspecified) {
            if (isDark) gaugeCenterTextColorDark else gaugeCenterTextColorLight
        } else {
            color
        }
    }
}
