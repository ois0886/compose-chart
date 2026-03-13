# Code Quality Guide

compose-chart 프로젝트의 코드 품질을 일관되게 유지하기 위한 가이드입니다.
새로운 코드를 작성하거나 기존 코드를 수정할 때 이 문서를 기준으로 합니다.

---

## 프로젝트 구조

```
compose-chart/          ← 라이브러리 모듈 (Maven Central 배포)
├── src/main/java/com/inseong/composechart/
│   ├── line/           ← LineChart Composable
│   ├── bar/            ← BarChart Composable
│   ├── donut/          ← DonutChart Composable
│   ├── gauge/          ← GaugeChart Composable
│   ├── radar/          ← RadarChart Composable
│   ├── pie/            ← PieChart Composable
│   ├── legend/         ← ChartLegend Composable
│   ├── data/           ← 데이터 클래스 (ChartPoint, *ChartData)
│   ├── style/          ← 스타일 클래스 (*ChartStyle, AxisStyle, GridStyle 등)
│   └── internal/       ← 외부 비공개
│       ├── math/       ← 순수 함수 (비즈니스 로직)
│       ├── animation/  ← 애니메이션 유틸
│       ├── touch/      ← 터치 핸들러
│       └── canvas/     ← Canvas 드로잉 유틸
├── src/test/           ← JVM 유닛 테스트
└── src/androidTest/    ← Compose UI 테스트

app/                    ← 샘플/데모 앱
```

---

## 코드 스타일

### 기본 규칙
- **Kotlin 공식 스타일** 적용 (`kotlin.code.style=official`)
- Trailing comma 사용

### 네이밍 컨벤션

| 대상 | 규칙 | 예시 |
|------|------|------|
| Composable 함수 | PascalCase | `LineChart`, `ChartLegend` |
| 상태 클래스 | `*State` suffix | `ChartZoomState` |
| remember helper | `remember*` prefix | `rememberChartZoomState()` |
| 팩토리 메서드 | 용도를 설명하는 이름 | `single()`, `fromValues()`, `fromMap()` |
| 순수 함수 모듈 | `*Math` suffix | `ChartMath`, `BarMath` |
| 스타일 클래스 | `*Style` suffix | `LineChartStyle`, `AxisStyle` |

### Import 규칙
- 와일드카드 import 허용: `kotlin.math.*` 등 표준 라이브러리에 한해 사용
- 사용하지 않는 import 제거

---

## 아키텍처 패턴

### Composable 함수

파라미터 순서: **data → modifier → style → colors → callbacks**

```kotlin
@Composable
fun LineChart(
    data: LineChartData,                                    // 1. 데이터 (필수)
    modifier: Modifier = Modifier,                         // 2. Modifier
    style: LineChartStyle = LineChartStyle(),               // 3. 스타일
    colors: List<Color> = ChartDefaults.colors,            // 4. 색상
    zoomState: ChartZoomState? = null,                     // 5. 상태 (선택)
    onPointSelected: ((Int, ChartPoint) -> Unit)? = null,  // 6. 콜백
)
```

- 모든 optional 파라미터에 기본값 제공
- 콜백은 nullable로 선언 (`(() -> Unit)? = null`)

### 데이터 클래스

```kotlin
data class LineChartData(
    val series: List<LineSeries>,
    val xLabels: List<String> = emptyList(),
) {
    companion object {
        fun single(points: List<ChartPoint>, ...): LineChartData { ... }
        fun fromMap(map: Map<String, Float>, ...): LineChartData { ... }
    }
}
```

- **불변** (`val`만 사용)
- **기본값 제공** (필수가 아닌 필드)
- **팩토리 메서드**: `companion object`에 편의 생성자 제공

### 스타일 클래스

```kotlin
data class LineChartStyle(
    val lineWidth: Dp = 2.dp,
    val curved: Boolean = true,
    val chart: ChartStyle = ChartStyle(),   // 중첩 스타일
    val axis: AxisStyle = AxisStyle(),
)
```

- **모든 필드에 기본값** — `LineChartStyle()`만으로 사용 가능해야 함
- **Composition 패턴** — 상속 대신 중첩 스타일 객체 사용
- 소비자는 `style.copy(lineWidth = 4.dp)`로 커스텀

### 순수 함수 (internal/math)

```kotlin
internal object ChartMath {
    fun calculateXYRange(
        xValues: List<Float>,
        yValues: List<Float>,
    ): XYRange { ... }
}
```

- **`internal object`** 로 그룹핑
- **부수 효과 없음** — 입력 → 출력만 존재
- **경계값 처리** — `coerceAtLeast(1f)`, `coerceIn()` 등으로 방어
- Compose 의존성 없이 JVM 테스트 가능

### 상태 관리

```kotlin
@Stable
class ChartZoomState(initialScale: Float = 1f) {
    var scale by mutableFloatStateOf(initialScale)
        internal set

    companion object {
        val Saver = Saver<ChartZoomState, List<Float>>( ... )
    }
}

@Composable
fun rememberChartZoomState(): ChartZoomState {
    return rememberSaveable(saver = ChartZoomState.Saver) { ChartZoomState() }
}
```

- **`@Stable`** 어노테이션으로 recomposition 최적화
- **`mutableFloatStateOf`** — 프리미티브 타입 전용 state 사용
- **`internal set`** — 외부에서 직접 변경 방지
- **Saver 패턴** — configuration change 시 상태 복원
- **`remember*` helper** — Composable에서 간편하게 사용

---

## 안전성 규칙

### NaN/Infinity 방어

```kotlin
data class ChartPoint(val x: Float, val y: Float) {
    internal val safeX: Float get() = if (x.isFinite()) x else 0f
    internal val safeY: Float get() = if (y.isFinite()) y else 0f
}
```

- 외부 입력을 받는 Float 값은 `isFinite()` 검증
- 안전한 기본값으로 대체 (0f)

### 빈 데이터 / 경계값

- 빈 리스트 입력 시 빈 차트를 렌더링 (크래시 금지)
- 음수 값 지원
- 값 범위 계산 시 `coerceAtLeast`, `coerceIn`으로 clamping

### 색상

- `Color.Unspecified`를 기본값으로 사용하여 테마 기반 자동 resolve

```kotlin
internal fun resolveGridLineColor(color: Color, isDark: Boolean): Color =
    if (color == Color.Unspecified) {
        if (isDark) gridLineColorDark else gridLineColorLight
    } else color
```

---

## 테스트 규칙

### 테스트 대상
- `internal/math/` 순수 함수 → **JVM 유닛 테스트** (`src/test/`)
- 상태 클래스 (Saver, 상태 변환) → **JVM 유닛 테스트**
- Composable 차트 렌더링/터치 → **Compose UI 테스트** (`src/androidTest/`)

### 테스트 네이밍

```
함수명_입력상황_기대결과
```

```kotlin
@Test
fun calculateXYRange_normalData_returnsCorrectRange() { ... }

@Test
fun calculateGroupWidth_zeroWidth_returnsMinimum() { ... }

@Test
fun applyZoom_exceedsMaxScale_clampsToMax() { ... }
```

### 작성 패턴 (AAA)

```kotlin
@Test
fun findTouchedGroupIndex_touchInSecondGroup_returnsOne() {
    // Arrange
    val chartWidth = 300f
    val groupCount = 3

    // Act
    val index = BarMath.findTouchedGroupIndex(touchX = 150f, chartWidth, groupCount, spacing = 10f)

    // Assert
    assertEquals(1, index)
}
```

### Float 비교

```kotlin
assertEquals(expected, actual, 0.001f)  // tolerance 필수
```

### 필수 테스트 케이스
- 정상 입력
- 빈 입력 (empty list, 0 크기)
- 경계값 (최소/최대, 0, 음수)
- NaN/Infinity 입력 (해당하는 경우)

### 새 기능 추가 시
- 비즈니스 로직은 순수 함수로 추출 → 유닛 테스트 작성
- 기존 테스트가 깨지지 않는지 확인: `./gradlew :compose-chart:test`

---

## 문서화

### KDoc (Public API 필수)

```kotlin
/**
 * 줌/팬 상태를 관리하는 상태 홀더.
 *
 * ```kotlin
 * val zoomState = rememberChartZoomState()
 * LineChart(data = data, zoomState = zoomState)
 * ```
 *
 * @param initialScale 초기 확대 배율 (기본값: 1f)
 * @param maxScale 최대 확대 배율 (기본값: 5f)
 */
@Stable
class ChartZoomState( ... )
```

- Public 클래스/함수에 KDoc 필수
- 사용 예시 코드 블록 포함
- `@param`, `@return` 명시

### CHANGELOG

[Keep a Changelog](https://keepachangelog.com/ko/1.0.0/) 형식:

```markdown
## [1.1.0] - 2026-03-13

### Added
- 새로운 기능 설명

### Changed
- 변경된 동작 설명

### Fixed
- 버그 수정 설명
```

---

## 커밋 규칙

### 형식

한국어 [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: 줌/팬 지원 추가 — ChartZoomState
fix: 빈 데이터셋에서 크래시 수정
refactor: 터치 핸들러 로직 분리
docs: README v1.1.0 기능 문서화
test: BarMath 경계값 테스트 추가
```

### 규칙
- prefix: `feat:`, `fix:`, `refactor:`, `docs:`, `test:`, `chore:`
- 본문은 한국어
- 하나의 커밋은 하나의 논리적 변경

---

## 빌드 및 검증

PR 제출 전 로컬에서 확인:

```bash
./gradlew :compose-chart:assembleDebug   # 라이브러리 빌드
./gradlew :compose-chart:test            # 유닛 테스트
./gradlew :compose-chart:lint            # Android Lint
```

### CI 파이프라인

`main` 브랜치 push/PR 시 자동 실행 (`.github/workflows/ci.yml`):

| Job | 내용 |
|-----|------|
| **build** | 라이브러리 빌드, 샘플 앱 빌드, 유닛 테스트, 린트 |
| **ui-test** | Android 에뮬레이터에서 Compose UI 테스트 |

CI가 통과해야 머지 가능합니다.
