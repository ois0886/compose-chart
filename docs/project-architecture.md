# Project Architecture

`compose-chart` is an Android open-source chart library built with Jetpack Compose and published to Maven Central as `io.github.ois0886:compose-chart`.

This document is for contributors and coding agents who need a quick, accurate map of the repository.

## Repository Roles

- `README.md` explains what the library does and how consumers use it.
- `compose-chart/` contains the publishable library.
- `app/` contains the sample gallery app for demos and manual verification.
- `CLAUDE.md` and `AGENTS.md` are agent-specific working guides.
- `CODE_QUALITY.md` defines implementation and review expectations.

## Module Structure

### `compose-chart/`

Library module published to Maven Central.

- Package: `com.inseong.composechart`
- Min SDK: 24
- Compile/Target SDK: 36
- Kotlin target: Java 11
- Dependency management: `gradle/libs.versions.toml`
- Publishing: `com.vanniktech.maven.publish` via Sonatype Central Portal

Main source layout:

- `data/` — chart input models such as `LineChartData`, `BarChartData`, `DonutChartData`
- `style/` — style models such as `LineChartStyle`, `AxisStyle`, `TooltipStyle`
- `line/`, `bar/`, `donut/`, `gauge/`, `radar/`, `pie/` — public chart composables
- `legend/` — `ChartLegend`
- `internal/math/` — pure calculation helpers for ranges, geometry, hit testing, and normalization
- `internal/canvas/` — reusable drawing helpers
- `internal/touch/` — gesture and hit-test helpers
- `internal/animation/` — animation helpers

Shared public state helpers:

- `ChartZoomState` with `rememberChartZoomState()` for zoom and pan
- `ChartCaptureState` with `rememberChartCaptureState()` and `Modifier.chartCaptureModifier()` for bitmap export

### `app/`

Sample app that showcases all charts and doubles as a manual verification surface.

- Entry point: `MainActivity.kt`
- Home screen: `ChartGalleryScreen.kt`
- Detail screens: one screen per chart type under `screen/`
- `SampleData.kt` includes normal, extreme, invalid, and empty datasets

## Supported Charts

- `LineChart` — multi-series line chart with optional curve, gradient fill, tooltip, zoom/pan
- `BarChart` — simple, grouped, stacked, and horizontal/vertical variations with tooltip and zoom/pan
- `DonutChart` — ring chart with slice labels and touch interaction
- `GaugeChart` — radial progress/gauge visualization
- `RadarChart` — radar/spider chart with axis labels
- `PieChart` — pie visualization backed by `DonutChartData`
- `ChartLegend` — reusable legend component for series toggles and labeling

## Design Principles

- Keep the library lightweight and Compose-first.
- Avoid Material3 in the library module.
- Prefer safe input handling over crashing on malformed data.
- Keep calculations testable by extracting non-UI logic into pure internal helpers.
- Expose simple public APIs with sensible defaults and convenience factories.

## Testing Strategy

### Library tests

- `compose-chart/src/test/java/...` is for JVM tests around pure math and logic helpers.
- `compose-chart/src/androidTest/java/...` is for Compose UI and rendering-related library tests.

### Sample app tests

- `app/src/androidTest/java/...` verifies navigation and each sample screen renders expected sections.
- These tests are useful smoke coverage for the gallery app and documentation examples.

## Build and Release Flow

Common commands:

```bash
./gradlew :compose-chart:assembleDebug
./gradlew :app:assembleDebug
./gradlew :compose-chart:test
./gradlew :compose-chart:lint
./gradlew :compose-chart:connectedAndroidTest
```

Release command:

```bash
./gradlew :compose-chart:publishAndReleaseToMavenCentral
```

GitHub Actions:

- `.github/workflows/ci.yml`
  - Builds the library and sample app
  - Runs `:compose-chart:test`
  - Runs `:compose-chart:lint`
  - Runs `:compose-chart:connectedAndroidTest` on an emulator
- `.github/workflows/publish.yml`
  - Runs tests and lint
  - Publishes the library on release or manual dispatch

## Contributor Notes

- Treat `README.md` as the source of truth for public-facing setup and examples.
- Keep internal contributor context in `docs/` instead of expanding agent instruction files.
- When behavior changes, update tests and user-facing docs together when appropriate.
- Follow `CODE_QUALITY.md` for implementation details such as immutability, pure functions, visibility, and named arguments.
