# compose-chart

Jetpack Compose에서 사용할 수 있는 가볍고 독립적인 차트 UI 컴포넌트 라이브러리입니다.

데이터만 넘기면 끝입니다.

[![Maven Central](https://img.shields.io/maven-central/v/io.github.ois0886/compose-chart)](https://central.sonatype.com/artifact/io.github.ois0886/compose-chart)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://developer.android.com/about/versions/nougat)

## Charts

<table>
  <tr>
    <td align="center"><b>Line Chart</b></td>
    <td align="center"><b>Bar Chart</b></td>
    <td align="center"><b>Donut Chart</b></td>
  </tr>
  <tr>
    <td><img src="screenshots/Line%20Chart.png" width="280"/></td>
    <td><img src="screenshots/Bar%20Chart.png" width="280"/></td>
    <td><img src="screenshots/Donut%20Chart.png" width="280"/></td>
  </tr>
  <tr>
    <td align="center"><b>Gauge Chart</b></td>
    <td align="center"><b>Radar Chart</b></td>
    <td align="center"><b>Pie Chart</b></td>
  </tr>
  <tr>
    <td><img src="screenshots/Gauge%20Chart.png" width="280"/></td>
    <td><img src="screenshots/Radar%20Chart.png" width="280"/></td>
    <td><img src="screenshots/Pie%20Chart.png" width="280"/></td>
  </tr>
</table>

## Why?

데이터 시각화는 앱의 핵심 기능이지만, Compose에서 바로 쓸 수 있는 가벼운 차트 라이브러리가 부족했습니다.

기존 접근 방식들은 Material3에 의존하거나, 설정이 복잡하거나, 특정 프로젝트와 강하게 결합되어 있었습니다. **compose-chart**는 이 문제를 해결하기 위해 만들었습니다:

- **UI 의존성 제로** — Compose Foundation만 사용합니다. Material3가 필요 없습니다.
- **6가지 차트** — Line, Bar, Donut, Gauge, Radar, Pie를 하나의 라이브러리로.
- **완전한 커스터마이징** — 색상, 축, 애니메이션, 터치 상호작용까지 모든 것을 파라미터로 설정할 수 있고, 기본값도 잘 잡혀 있습니다.
- **안전한 입력 처리** — NaN, Infinity, 음수, 빈 데이터도 크래시 없이 안전하게 처리합니다.

## Setup

```kotlin
// build.gradle.kts
dependencies {
    implementation("io.github.ois0886:compose-chart:1.2.0")
}
```

## Quick Start

### 선 차트

<img src="screenshots/Line%20Chart.png" width="400"/>

```kotlin
LineChart(
    data = LineChartData.fromValues(
        values = listOf(10f, 25f, 18f, 32f, 22f),
        xLabels = listOf("1월", "2월", "3월", "4월", "5월"),
    ),
    modifier = Modifier.fillMaxWidth().height(200.dp),
)
```

### 막대 차트

<img src="screenshots/Bar%20Chart.png" width="400"/>

```kotlin
BarChart(
    data = BarChartData.simple(
        values = listOf(30f, 45f, 25f, 60f),
        labels = listOf("1분기", "2분기", "3분기", "4분기"),
    ),
    modifier = Modifier.fillMaxWidth().height(200.dp),
)
```

### 도넛 차트

<img src="screenshots/Donut%20Chart.png" width="300"/>

```kotlin
DonutChart(
    data = DonutChartData(
        slices = listOf(
            DonutSlice(40f, "식비"),
            DonutSlice(25f, "교통"),
            DonutSlice(35f, "기타"),
        ),
    ),
    modifier = Modifier.size(200.dp),
)
```

### 게이지 차트

<img src="screenshots/Gauge%20Chart.png" width="300"/>

```kotlin
GaugeChart(
    data = GaugeChartData(value = 72f, maxValue = 100f, label = "점수"),
    modifier = Modifier.size(180.dp),
)
```

### 레이더 차트

<img src="screenshots/Radar%20Chart.png" width="300"/>

```kotlin
RadarChart(
    data = RadarChartData.single(
        values = listOf(80f, 65f, 90f, 70f, 85f),
        axisLabels = listOf("STR", "DEX", "INT", "WIS", "CHA"),
    ),
    modifier = Modifier.size(200.dp),
)
```

### 파이 차트

<img src="screenshots/Pie%20Chart.png" width="300"/>

```kotlin
PieChart(
    data = DonutChartData.fromValues(30f, 25f, 45f),
    modifier = Modifier.size(200.dp),
)
```

## Features (v1.2.0)

### 줌 & 팬

Line, Bar 차트에서 핀치 줌과 드래그 팬을 지원합니다. 더블탭으로 초기화됩니다.

```kotlin
val zoomState = rememberChartZoomState()

LineChart(
    data = data,
    zoomState = zoomState,
)
```

### 차트 이미지 캡처

차트를 `ImageBitmap`으로 내보낼 수 있습니다.

```kotlin
val captureState = rememberChartCaptureState()

LineChart(
    data = data,
    modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .chartCaptureModifier(captureState),
)

// 캡처 (suspend)
val bitmap: ImageBitmap = captureState.capture()
```

### 인터랙티브 범례

범례 항목을 탭하여 시리즈를 토글할 수 있습니다.

```kotlin
val items = remember {
    mutableStateListOf(
        LegendItem(color = Color.Blue, label = "매출", enabled = true),
        LegendItem(color = Color.Red, label = "비용", enabled = true),
    )
}

ChartLegend(
    items = items,
    onItemClick = { index ->
        items[index] = items[index].copy(enabled = !items[index].enabled)
    },
)
```

### 팩토리 메서드

데이터 객체를 간결하게 생성할 수 있습니다.

```kotlin
// 그룹 막대 차트
BarChartData.grouped(
    seriesValues = listOf(listOf(10f, 20f), listOf(15f, 25f)),
    labels = listOf("1월", "2월"),
)

// Map에서 멀티 시리즈 라인 차트
LineChartData.fromMap(
    seriesMap = mapOf("매출" to listOf(10f, 20f), "비용" to listOf(5f, 15f)),
    xLabels = listOf("Q1", "Q2"),
)

// 값만으로 도넛 차트
DonutChartData.fromValues(40f, 25f, 20f, 15f)
```

### Y축 범위 수동 설정

```kotlin
LineChart(
    data = data,
    style = LineChartStyle(
        axis = AxisStyle(
            showYAxis = true,
            yAxisMin = 0f,    // 최솟값 고정
            yAxisMax = 100f,  // 최댓값 고정
        ),
    ),
)
```

### 폰트 커스텀

축, 툴팁, 범례, 게이지 중앙 텍스트의 폰트 굵기를 설정할 수 있습니다.

```kotlin
LineChart(
    data = data,
    style = LineChartStyle(
        axis = AxisStyle(fontWeight = FontWeight.Bold),
        tooltip = TooltipStyle(fontWeight = FontWeight.Medium),
    ),
)
```

## Customization

### 색상

```kotlin
// 커스텀 색상 팔레트
LineChart(
    data = data,
    colors = listOf(Color.Red, Color.Blue, Color.Green),
)

// 시리즈별 개별 색상
LineChart(
    data = LineChartData(
        series = listOf(
            LineSeries(points = points1, color = Color.Red),
            LineSeries(points = points2, color = Color.Blue),
        ),
    ),
)
```

### 스타일

```kotlin
LineChart(
    data = data,
    style = LineChartStyle(
        curved = true,              // 곡선/직선
        showDots = true,            // 데이터 포인트 표시
        gradientFill = true,        // 그래디언트 채우기
        lineWidth = 3.dp,           // 선 두께
        animationDurationMs = 1000, // 애니메이션 시간
        axis = AxisStyle(
            showYAxis = true,
            yLabelCount = 5,
            yAxisFormatter = { value -> "${value.toLong()}%" },
        ),
        grid = GridStyle(
            showHorizontalLines = true,
            dashPattern = floatArrayOf(10f, 5f),
        ),
    ),
)
```

### 터치 상호작용

```kotlin
LineChart(
    data = data,
    onPointSelected = { seriesIndex, pointIndex, point ->
        println("시리즈 $seriesIndex, 포인트 $pointIndex: ${point.y}")
    },
)

DonutChart(
    data = data,
    onSliceSelected = { index, slice ->
        println("${slice.label}: ${slice.value}")
    },
)
```

### 범례

```kotlin
Column {
    LineChart(data = data, colors = colors)
    ChartLegend(
        items = data.series.mapIndexed { i, s ->
            LegendItem(color = colors[i % colors.size], label = s.label)
        },
    )
}
```

## Chart Types

| 차트 | Composable | 데이터 클래스 | 용도 |
|------|-----------|-------------|------|
| 선 차트 | `LineChart` | `LineChartData` | 시계열, 추세 |
| 막대 차트 | `BarChart` | `BarChartData` | 비교, 분포 |
| 도넛 차트 | `DonutChart` | `DonutChartData` | 비율, 구성 |
| 게이지 차트 | `GaugeChart` | `GaugeChartData` | 진행도, 달성률 |
| 레이더 차트 | `RadarChart` | `RadarChartData` | 다축 비교 |
| 파이 차트 | `PieChart` | `DonutChartData` | 비율 (도넛의 특수 케이스) |

## Development Highlights

- **Pure Compose Foundation** — Material3 의존 없이 Foundation의 `Canvas`, `BasicText`만 사용하여, 어떤 디자인 시스템을 쓰는 프로젝트에서도 충돌 없이 동작합니다.
- **안전한 데이터 처리** — NaN, Infinity, 음수, 빈 데이터를 모든 차트에서 방어적으로 처리합니다. `safeX`, `safeY` 패턴으로 잘못된 입력이 크래시를 일으키지 않습니다.
- **순수 함수 & 테스트** — 좌표 계산, 범위 계산, 터치 감지 로직이 `internal/math/`에 순수 함수로 분리되어 있으며, 72개의 JVM 유닛 테스트와 183개의 UI 테스트가 전체 차트를 검증합니다.
- **줌 & 팬** — `ChartZoomState`를 통한 핀치 줌/드래그 팬 지원. Canvas `withTransform`으로 좌표 역변환 처리.
- **이미지 캡처** — `GraphicsLayer` API를 활용하여 차트를 `ImageBitmap`으로 내보내기.
- **테마 인식** — `Color.Unspecified` 패턴으로 다크/라이트 테마에 맞는 기본 색상을 자동 적용합니다.
- **접근성 지원** — 각 차트에 `semantics`로 콘텐츠 설명을 제공하여 스크린 리더(TalkBack)를 지원합니다.

## Requirements

- Min SDK 24 (Android 7.0)
- Jetpack Compose (Foundation)

## License

```
Copyright 2026 Inseong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
