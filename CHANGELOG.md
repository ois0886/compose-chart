# Changelog

이 프로젝트의 모든 주요 변경 사항을 기록합니다.
[Keep a Changelog](https://keepachangelog.com/ko/1.1.0/) 형식을 따릅니다.

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
