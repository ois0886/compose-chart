# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

compose-chart is an Android open-source library for custom chart UI components built with Jetpack Compose. The app module currently serves as a demo/sample app for developing and showcasing chart composables.

## Build Commands

```bash
./gradlew assembleDebug          # Build debug APK
./gradlew test                   # Run unit tests (JVM)
./gradlew connectedAndroidTest   # Run instrumented tests (requires device/emulator)
./gradlew lint                   # Run Android Lint
./gradlew clean                  # Clean build artifacts
```

Run a single test class:
```bash
./gradlew test --tests "com.example.compose_chart.ExampleUnitTest"
```

## Architecture

- **Single module (`app/`)** — currently houses both the demo app and chart components
- **UI framework**: Jetpack Compose with Material3
- **Min SDK**: 24 | **Compile/Target SDK**: 36
- **Kotlin JVM target**: 11
- **Dependency management**: Gradle version catalog (`gradle/libs.versions.toml`)

### Key Directories

- `app/src/main/java/com/example/compose_chart/` — Main source (MainActivity, chart composables)
- `app/src/main/java/com/example/compose_chart/ui/theme/` — Material3 theme (Color, Theme, Typography)
- `app/src/test/` — JUnit 4 unit tests
- `app/src/androidTest/` — Instrumented tests (Espresso, Compose UI tests)

### Theme System

Material3 with dynamic color support (Android 12+). Custom palette defined in `Color.kt`, theme wiring in `Theme.kt`.
