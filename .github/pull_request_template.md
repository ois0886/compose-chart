## 요약

-

## 변경 내용

-

## 검증

- [ ] `./gradlew :compose-chart:assembleDebug`
- [ ] `./gradlew :app:assembleDebug`
- [ ] `./gradlew :compose-chart:testDebugUnitTest`
- [ ] `./gradlew :compose-chart:lint`
- [ ] `./gradlew :compose-chart:connectedDebugAndroidTest` (UI/렌더링/접근성 변경 시)
- [ ] 실행하지 않은 검증이 있다면 이유를 적었습니다.

## 체크리스트

- [ ] `compose-chart/` 라이브러리 모듈에 Material3 의존성을 추가하지 않았습니다.
- [ ] 계산/변환 로직은 가능한 한 순수 함수로 분리했습니다.
- [ ] 동작 변경에는 테스트를 추가하거나 갱신했습니다.
- [ ] README 예제를 바꾼 경우 mirrored example test도 갱신했습니다.
- [ ] public behavior, docs, accessibility output, release-visible workflow 변경 시 `CHANGELOG.md`를 갱신했습니다.

## 관련 이슈

-
