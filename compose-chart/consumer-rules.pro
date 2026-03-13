# compose-chart consumer ProGuard rules

# Chart defaults
-keep class com.inseong.composechart.ChartDefaults { *; }

# Data classes
-keep class com.inseong.composechart.data.** { *; }

# Style classes
-keep class com.inseong.composechart.style.** { *; }

# Legend
-keep class com.inseong.composechart.legend.** { *; }

# Chart composables
-keep class com.inseong.composechart.line.LineChartKt { *; }
-keep class com.inseong.composechart.bar.BarChartKt { *; }
-keep class com.inseong.composechart.donut.DonutChartKt { *; }
-keep class com.inseong.composechart.pie.PieChartKt { *; }
-keep class com.inseong.composechart.gauge.GaugeChartKt { *; }
-keep class com.inseong.composechart.radar.RadarChartKt { *; }

# Zoom/Pan state
-keep class com.inseong.composechart.ChartZoomState { *; }
-keep class com.inseong.composechart.ChartZoomStateKt { *; }

# Chart capture
-keep class com.inseong.composechart.ChartCaptureState { *; }
-keep class com.inseong.composechart.ChartCaptureStateKt { *; }

# Enums
-keepclassmembers enum com.inseong.composechart.style.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
