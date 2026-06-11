# Code Quality Guide

compose-chart 프로젝트의 코드 품질을 일관되게 유지하기 위한 가이드입니다.
새로운 코드를 작성하거나 기존 코드를 수정할 때 이 문서를 기준으로 합니다.

> **작업 시작 전 필수 확인**: `CLAUDE.md`(프로젝트 구조/빌드/워크플로우)와 `CODE_QUALITY.md`(본 문서)를 반드시 읽고 작업을 시작한다.

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

## Kotlin 코드 원칙

### 가변성 제한

- **`val` 우선** — 변경할 필요가 없는 변수는 반드시 `val`로 선언한다.
- **불변 컬렉션 우선** — `List`, `Set`, `Map`을 기본으로 사용하고, `MutableList` 등은 빌드 시점에만 한정한다.
- 상태 변경이 필요하면 `data class`의 `copy()`를 활용한다.
- 가변 객체를 외부에 노출하지 않는다.

```kotlin
// Bad
var items = mutableListOf<ChartPoint>()

// Good
val items: List<ChartPoint> = buildList { ... }
```

### 변수 스코프 최소화

- 변수는 사용하는 곳에서 가장 가까운 스코프에 선언한다.
- 클래스 프로퍼티보다 지역 변수, 반복문 내부 선언을 우선한다.
- 스코프가 넓을수록 상태 추적이 어렵고 오용 가능성이 높아진다.

### 가시성 최소화

- 공개할 필요 없는 요소는 `private` 또는 `internal`로 제한한다.
- 라이브러리 내부 구현은 `internal` modifier를 명시한다.
- 상태 클래스의 setter는 `internal set`으로 캡슐화한다.

```kotlin
var scale by mutableFloatStateOf(initialScale)
    internal set  // 외부에서 직접 변경 방지
```

### 타입 명시

- 추론 타입만으로 의미가 불분명한 경우 타입을 명시적으로 지정한다.
- Public API의 반환 타입은 반드시 명시한다.

```kotlin
// Bad — 반환 타입이 불명확
fun createDefault() = LineChartStyle(lineWidth = 2.dp)

// Good — 반환 타입 명시
fun createDefault(): LineChartStyle = LineChartStyle(lineWidth = 2.dp)
```

### null 안전 처리

- Safe call(`?.`), Elvis 연산자(`?:`)를 활용한다.
- **`!!` 사용 금지** — 불가피한 경우 `requireNotNull()` 또는 `checkNotNull()`로 대체한다.
- nullable 타입을 반환하는 함수는 `*OrNull` 네이밍을 사용한다.

```kotlin
// Bad
val point = points.find { it.x == targetX }!!

// Good
val point = points.find { it.x == targetX }
    ?: return  // early return 또는 기본값
```

### 방어적 검증

- **`require`** — 함수 인자 검증 (실패 시 `IllegalArgumentException`)
- **`check`** — 상태 검증 (실패 시 `IllegalStateException`)
- 사용자 정의 예외보다 표준 예외를 우선 사용한다.

```kotlin
fun setScale(scale: Float) {
    require(scale > 0f) { "Scale must be positive: $scale" }
    require(scale <= maxScale) { "Scale must not exceed $maxScale: $scale" }
    this.scale = scale
}
```

### 이름 있는 아규먼트

- 파라미터가 3개 이상이거나, 같은 타입의 파라미터가 연속되면 이름 있는 아규먼트를 사용한다.
- Boolean 파라미터는 항상 이름을 명시한다.

```kotlin
// Bad — 어떤 값이 무엇인지 불명확
calculateGroupWidth(300f, 3, 10f)

// Good
calculateGroupWidth(
    chartWidth = 300f,
    groupCount = 3,
    spacing = 10f,
)
```

### 프로퍼티는 상태, 함수는 동작

- 프로퍼티는 상태를 나타내고, 계산이 필요한 동작은 함수로 정의한다.
- 프로퍼티 getter에 무거운 계산을 넣지 않는다.

```kotlin
// Good — 프로퍼티: 상태
val isZoomed: Boolean get() = scale > minScale + 0.01f

// Good — 함수: 동작/계산
fun calculateXYRange(xValues: List<Float>, yValues: List<Float>): XYRange { ... }
```

### 함수 추상화 레벨 통일

- 하나의 함수 내에서 추상화 레벨을 혼합하지 않는다.
- 고수준 로직과 저수준 세부사항을 같은 함수에 두지 않고, 저수준 로직은 별도 함수로 추출한다.

### 표준 라이브러리 활용

- 일반적인 알고리즘을 직접 구현하지 않는다.
- `map`, `filter`, `groupBy`, `associate`, `sumOf` 등 Kotlin 표준 라이브러리를 활용한다.
- `filterNotNull()`, `mapNotNull()`, `filterIsInstance<T>()` 등 특화 함수를 사용하여 처리 단계를 줄인다.

### 연산자 오버로딩

- 연산자 오버로딩은 의미가 명확할 때만 사용한다.
- 의미가 모호하면 일반 함수로 정의한다.

---

## 아키텍처 패턴

### Composable 함수

파라미터 순서: **data → modifier → style → colors → state → accessibility → callbacks**

```kotlin
@Composable
fun LineChart(
    data: LineChartData,                                         // 1. 데이터 (필수)
    modifier: Modifier = Modifier,                               // 2. Modifier
    style: LineChartStyle = LineChartStyle(),                    // 3. 스타일
    colors: List<Color> = ChartDefaults.colors,                  // 4. 색상
    zoomState: ChartZoomState? = null,                           // 5. 상태 (선택)
    accessibilityLabel: String = "선 차트",                       // 6. 접근성 라벨
    onClickLabel: String? = null,                                // 7. 스크린리더 클릭 라벨
    onSelectionChanged: ((ChartSelection.Line) -> Unit)? = null, // 8. 공통 선택 콜백
    onPointSelected: ((Int, Int, ChartPoint) -> Unit)? = null,   // 9. 레거시 콜백 (v2.0 제거 예정)
)
```

- 모든 optional 파라미터에 기본값 제공
- 콜백은 함수 타입으로 선언 (`(() -> Unit)? = null`) — SAM 인터페이스 대신 함수 타입 사용
- 접근성 파라미터는 6개 차트 전부에서 동일하게 `accessibilityLabel`/`onClickLabel` 쌍으로 노출
- 선택 이벤트는 `ChartSelection` sealed interface 하위 타입을 방출하는 `onSelectionChanged`를 우선 사용하고, 차트별 레거시 콜백(`onPointSelected` 등)은 소스 호환을 위해 병존

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

- **불변** (`val`만 사용) — 가변성 제한 원칙
- **기본값 제공** (필수가 아닌 필드) — 이름 있는 옵션 아규먼트로 빌더 패턴 대체
- **팩토리 메서드**: `companion object`에 편의 생성자 제공 — 생성자 대신 팩토리 함수 활용

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

### 클래스 설계

- **상속보다 컴포지션** — 단일 상속의 제약을 피하고, 필요한 기능만 조합한다.
- **sealed class 활용** — 태그 기반 분기 대신 sealed class 계층으로 타입 안전성 확보.
- **`data` 한정자** — 데이터 집합에는 반드시 `data class` 사용 (`toString`, `equals`, `hashCode`, `copy` 자동 생성).

### 순수 함수 (internal/math)

```kotlin
internal object ChartMath {
    fun calculateXYRange(
        xValues: List<Float>,
        yValues: List<Float>,
    ): XYRange { ... }
}
```

- **`internal object`** 로 그룹핑 — 불필요한 객체 생성 방지
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
- **`mutableFloatStateOf`** — 프리미티브 타입 전용 state 사용 (불필요한 박싱 방지)
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

### 리소스 관리

- `Closeable` / `AutoCloseable` 리소스는 `use` 블록으로 자동 정리한다.
- 더 이상 사용하지 않는 객체의 레퍼런스는 제거하여 GC 효율을 높인다.

---

## 효율성 규칙

### 불필요한 객체 생성 방지

- 반복 호출되는 곳에서 매번 새 객체를 생성하지 않는다.
- 싱글톤(`object`), 캐싱, 지연 초기화(`lazy`)를 적절히 활용한다.
- 성능이 중요한 계산에서는 기본 자료형 배열(`FloatArray`, `IntArray`)을 사용한다.

### Canvas 렌더링 최적화

- `Canvas`/draw 블록 안에서는 상태 변경, 외부 콜백 호출, 선택 이벤트 방출을 하지 않는다.
- 터치 hit-test 결과와 접근성 선택 설명은 draw 밖에서 파생 상태로 계산하고, 콜백은 선택값 변경 시점에만 호출한다.
- 데이터/크기/style에만 의존하는 좌표, `Path`, 라벨 목록, 누적값은 `remember`, `lazy`, `drawWithCache` 등으로 재사용한다.
- draw 중 반복 생성되는 `Paint`, `PathEffect`, 임시 컬렉션은 호출 단위 캐시나 더 직접적인 draw API로 줄인다.
- 정렬된 좌표 탐색은 `FloatArray` 같은 primitive 배열과 binary search를 우선하고, 정렬되지 않은 사용자 데이터에는 기존 순서 기반 fallback을 보존한다.
- range/layout 계산은 가능하면 composition 단계에서 single-pass로 끝내고, draw 단계에는 animation progress에 필요한 계산만 남긴다.

### 컬렉션 처리 최적화

- 여러 단계의 컬렉션 처리는 `Sequence`를 고려한다 (지연 처리, 최소 연산).
- 컬렉션 처리 단계 수를 제한하고, 특화 함수를 활용한다.

```kotlin
// Bad — 2단계
list.map { it.toNullable() }.filterNotNull()

// Good — 1단계
list.mapNotNull { it.toNullable() }
```

### inline 활용

- 함수 타입 파라미터를 받는 고차 함수에는 `inline`을 고려한다.
- 단일 프로퍼티 래퍼 클래스는 `value class`(inline class)를 고려한다.

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
- 기존 테스트가 깨지지 않는지 확인: `./gradlew :compose-chart:testDebugUnitTest`
- README 코드 예제를 변경하면 대응되는 예제 테스트도 함께 갱신
- 접근성 semantics를 변경하면 UI 테스트에서 설명 문자열과 선택 상태를 함께 검증

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

- 공개 동작, 문서 예제, 접근성 출력, 릴리스 운영 흐름이 바뀌면 같은 변경에 `CHANGELOG.md`를 포함한다.
- 진행 중인 변경은 `Unreleased` 섹션에 먼저 기록하고, 릴리스 시 버전 섹션으로 정리한다.

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
./gradlew :compose-chart:assembleDebug        # 라이브러리 빌드
./gradlew :compose-chart:testDebugUnitTest    # JVM 유닛 테스트 (AGP 9: variant-specific 태스크 사용)
./gradlew :compose-chart:lint                 # Android Lint
```

### CI 파이프라인

`main` 브랜치 push/PR 시 자동 실행 (`.github/workflows/ci.yml`):

| Job | 내용 |
|-----|------|
| **build** | 라이브러리 빌드, 샘플 앱 빌드, 유닛 테스트, 린트 |
| **ui-test** | Android 에뮬레이터에서 Compose UI 테스트 |

CI가 통과해야 머지 가능합니다.
