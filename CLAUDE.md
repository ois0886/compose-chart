# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

compose-chart is an Android open-source library for custom chart UI components built with Jetpack Compose. Published to Maven Central via `io.github.oinseong:compose-chart`.

## Module Structure

- **`compose-chart/`** — 라이브러리 모듈. 차트 Composable 컴포넌트가 위치. Maven Central로 배포됨.
- **`app/`** — 샘플/데모 앱. 라이브러리 사용 예시를 보여줌.

## Build Commands

```bash
./gradlew :compose-chart:assembleDebug   # 라이브러리 빌드
./gradlew :app:assembleDebug             # 샘플 앱 빌드
./gradlew test                           # 전체 유닛 테스트
./gradlew :compose-chart:test            # 라이브러리 유닛 테스트만
./gradlew lint                           # Android Lint
./gradlew clean                          # 빌드 정리
```

Run a single test class:
```bash
./gradlew :compose-chart:test --tests "com.inseong.composechart.SomeTest"
```

Publish to Maven Central:
```bash
./gradlew :compose-chart:publishAndReleaseToMavenCentral
```

## Architecture

- **Package**: `com.inseong.composechart`
- **groupId**: `io.github.oinseong` / **artifactId**: `compose-chart`
- **UI framework**: Jetpack Compose (no Material3 dependency in library — consumer decides)
- **Min SDK**: 24 | **Compile/Target SDK**: 36
- **Kotlin JVM target**: 11
- **Dependency management**: Gradle version catalog (`gradle/libs.versions.toml`)
- **Publishing**: vanniktech/gradle-maven-publish-plugin → Sonatype Central Portal

### Chart Components

| 차트 | 패키지 | 데이터 클래스 | 스타일 클래스 |
|------|--------|--------------|-------------|
| LineChart | `line/` | `LineChartData` | `LineChartStyle` |
| BarChart | `bar/` | `BarChartData` | `BarChartStyle` |
| DonutChart | `donut/` | `DonutChartData` | `DonutChartStyle` |
| GaugeChart | `gauge/` | `GaugeChartData` | `GaugeChartStyle` |
| ScatterChart | `scatter/` | `ScatterChartData` | `ScatterChartStyle` |
| BubbleChart | `bubble/` | `BubbleChartData` | `BubbleChartStyle` |
| RadarChart | `radar/` | `RadarChartData` | `RadarChartStyle` |
| PieChart | `pie/` | `DonutChartData` (재사용) | `PieChartStyle` |

### Key Directories

- `compose-chart/src/main/java/com/inseong/composechart/` — 차트 라이브러리 소스
- `compose-chart/src/androidTest/` — Compose UI 테스트 (전체 차트 + ChartSize)
- `app/src/main/java/com/inseong/composechart/` — 샘플 앱 소스
- `app/src/main/java/com/inseong/composechart/ui/theme/` — Material3 테마

## Workflow Rules

### 1. Plan Mode First (필수)
- 모든 개발 작업 시작 전에 반드시 `/plan` 모드로 진입하여 구현 계획을 수립한다.
- 계획 단계에서: 영향 범위 파악, 파일 구조 분석, 구현 전략 결정, 테스트 전략 수립.
- 계획이 확정된 후에 코드 작성을 시작한다.

### 2. CLAUDE.md 우선 확인
- 작업 시작 전 반드시 CLAUDE.md를 읽고 프로젝트 컨벤션과 지침을 따른다.

### 3. 테스트 가능한 코드 작성 원칙
- **관심사 분리**: UI 로직과 비즈니스 로직을 분리한다. 계산/변환 로직은 순수 함수로 추출하여 단위 테스트가 가능하게 한다.
- **의존성 주입**: 외부 의존성은 파라미터로 주입받아 테스트 시 교체 가능하게 한다.
- **순수 함수 우선**: 부수 효과(side effect) 없는 순수 함수를 우선 사용한다. 입력 → 출력이 명확한 함수는 테스트가 쉽다.
- **작은 단위**: 하나의 함수/컴포저블은 하나의 책임만 가진다. 큰 함수는 테스트 가능한 작은 함수로 분리한다.
- **테스트 작성**: 새로운 기능 추가 시 유닛 테스트를 함께 작성한다. 기존 테스트가 깨지지 않는지 확인한다.

## CI

GitHub Actions (`.github/workflows/ci.yml`) — `main` 브랜치 push/PR 시 자동 실행:
- **build** job: 라이브러리 빌드, 샘플 앱 빌드, 유닛 테스트, 린트
- **ui-test** job: Android 에뮬레이터에서 Compose UI 테스트 (`connectedAndroidTest`)
