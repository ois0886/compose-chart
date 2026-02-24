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

### Key Directories

- `compose-chart/src/main/java/com/inseong/composechart/` — 차트 라이브러리 소스
- `app/src/main/java/com/inseong/composechart/` — 샘플 앱 소스
- `app/src/main/java/com/inseong/composechart/ui/theme/` — Material3 테마
