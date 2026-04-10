# Changelog

이 프로젝트의 모든 주요 변경 사항을 기록합니다.
[Keep a Changelog](https://keepachangelog.com/ko/1.0.0/) 형식을 따릅니다.

## [Unreleased]

### Added
- README Quick Start 예제를 코드로 미러링하는 테스트 추가 — 문서 예제와 실제 API 시그니처 동기화 검증
- 차트 접근성 검증 테스트 추가 — contentDescription 및 선택 상태 설명 확인

### Changed
- 샘플 앱 차트 상세 화면 공통 showcase scaffold 도입 — 화면 중복 구조 정리
- Line, Bar, Donut, Pie, Gauge, Radar 차트 semantics 설명 강화 — 차트 요약, 값, 선택 상태 전달 개선

### Documentation
- README에 접근성 지원 및 예제 검증 방식 설명 추가
- 기여 가이드 문서에 README 예제 테스트와 CHANGELOG 갱신 규칙 명시

## [1.2.0] - 2026-03-13

### Changed
- `GridStyle.dashPattern` 타입을 `FloatArray?`에서 `List<Float>?`로 변경 — 불변성 보장 및 data class `equals()`/`hashCode()` 정합성 개선
- `BarEntry.safeValues`, `RadarEntry.safeValues`를 `lazy` 초기화로 변경 — 불필요한 객체 반복 생성 방지
- `ChartDefaults.colors` 타입 명시 (`List<Color>`)
- `BarMath.calculateAdjustedMax()` NaN/Infinity 입력 방어 추가
- LineChart, BarChart 내부 함수 호출에 이름 있는 아규먼트 적용 — 가독성 개선

### Added
- `CODE_QUALITY.md` — 코드 품질 가이드 문서 (Kotlin 코드 원칙, 테스트 규칙, 아키텍처 패턴)
- 팩토리 메서드 빈 입력/경계값 테스트 추가
- BarMath, ChartMath, RadarMath NaN/Infinity/경계값 테스트 추가

## [1.1.0] - 2026-03-13

### Added
- 줌/팬 지원 — `ChartZoomState`, `rememberChartZoomState()` (Line, Bar 차트)
- 차트 이미지 내보내기 — `ChartCaptureState`, `Modifier.chartCaptureModifier()`
- 인터랙티브 범례 — `ChartLegend`에 `onItemClick` 콜백, `LegendItem.enabled` 상태
- 팩토리 메서드 — `BarChartData.grouped()`, `LineChartData.fromMap()`, `DonutChartData.fromValues(vararg)`
- 폰트 커스텀 — `AxisStyle.fontWeight`, `TooltipStyle.fontWeight`, `LegendStyle.fontWeight`, `RadarChartStyle.labelFontWeight`, `GaugeChartStyle.centerFontWeight`
- Y축 범위 수동 설정 — `AxisStyle.yAxisMin`, `AxisStyle.yAxisMax`
- ProGuard consumer rules — 라이브러리 공개 API 보존 규칙

## [1.0.0] - 2026-03-12

### Changed
- groupId를 `io.github.ois0886`로 통일
- ScatterChart, BubbleChart 제거 — 6개 차트로 정리
- 버전 1.0.0 정식 릴리스

### Added
- GitHub Actions CD 워크플로우 — Release 생성 시 Maven Central 자동 배포

## [0.2.0] - 2026-03-12

### Added
- 범례(Legend) 컴포넌트 — `ChartLegend`, `LegendItem`, `LegendStyle`
- Y축 커스텀 포맷터 — `AxisStyle.yAxisFormatter` 파라미터
- 데이터 변경 시 전환 애니메이션 — data 변경마다 자동 재애니메이션
- 접근성(Accessibility) 지원 — 각 차트에 `semantics` contentDescription 추가
- README.md 한국어 문서화
- CHANGELOG.md 추가

### Changed
- `rememberChartAnimation`에 `animationKey` 파라미터 추가
- `drawYAxisLabels`에 커스텀 포맷터 지원

## [0.1.0] - 초기 릴리스

### Added
- 6개 차트 컴포넌트 (Line, Bar, Donut, Gauge, Radar, Pie)
- 터치 상호작용 및 툴팁
- 진입 애니메이션
- 다크/라이트 테마 지원
- Maven Central 배포
