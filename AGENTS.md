# AGENTS.md

This file provides guidance to Codex when working with code in this repository.

## Purpose

- `README.md` — public product documentation for library users
- `CLAUDE.md` — Claude-focused repository guidance
- `AGENTS.md` — Codex-focused working rules
- `docs/project-architecture.md` — internal architecture and onboarding reference
- `CODE_QUALITY.md` — coding and review quality bar

## Start Here

Before making changes, read documents in this order:

1. `AGENTS.md`
2. `CODE_QUALITY.md`
3. `docs/project-architecture.md`
4. `README.md` when public API or examples are relevant

## Required Workflow

### 1. Plan first

- Start in plan mode before implementation.
- Confirm scope, impacted files, risks, and test strategy before editing.

### 2. Keep roles separated

- Treat `README.md` as user-facing documentation.
- Put internal contributor or agent onboarding content in `docs/`.
- Keep `AGENTS.md` short and operational. Do not duplicate long architecture sections here.

### 3. Preserve library boundaries

- `compose-chart/` is the publishable library module.
- `app/` is the sample app used for demos and manual verification.
- Do not introduce Material3 dependencies into the library module.

### 4. Write testable code

- Follow `CODE_QUALITY.md`.
- Prefer pure functions for calculations and transformations.
- Keep UI rendering and business/math logic separated.
- Add or update tests when behavior changes.
- When changing README code examples, update the mirrored example tests too.
- When changing public behavior, docs, accessibility output, or release-visible workflows, update `CHANGELOG.md` in the same change.

### 5. Finish with validation and commit

- Run the narrowest useful verification for the change.
- Review the resulting diff before commit.
- Commit completed work with a Korean conventional commit message.
- When opening a PR, create it as ready for review, not draft, unless the user explicitly asks for a draft PR.

## Build Commands

```bash
./gradlew :compose-chart:assembleDebug
./gradlew :app:assembleDebug
./gradlew :compose-chart:testDebugUnitTest
./gradlew :compose-chart:lint
./gradlew :compose-chart:connectedDebugAndroidTest
./gradlew :compose-chart:publishAndReleaseToMavenCentral
```

> AGP 9 note: the aggregate `test` task no longer accepts `--tests`. Use
> `testDebugUnitTest` for single-class filters.

## Key Paths

- `compose-chart/src/main/java/com/inseong/composechart/` — library source
- `compose-chart/src/test/java/com/inseong/composechart/` — JVM tests
- `compose-chart/src/androidTest/java/com/inseong/composechart/` — library UI tests
- `app/src/main/java/com/inseong/composechart/` — sample app source
- `.github/workflows/ci.yml` — CI workflow
- `.github/workflows/publish.yml` — Maven Central publish workflow
