#!/usr/bin/env bash

set -euo pipefail

readonly ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
readonly PACKAGE_NAME="com.inseong.composechart.sample"
readonly TEST_RUNNER="$PACKAGE_NAME.test/androidx.test.runner.AndroidJUnitRunner"
readonly TEST_CLASS="com.inseong.composechart.ReadmeScreenshotTest"
readonly DEVICE_DIRECTORY="/sdcard/Android/data/$PACKAGE_NAME/files/readme-screenshots"
readonly SCREENSHOT_DIRECTORY="$ROOT_DIR/screenshots"
readonly APP_APK="$ROOT_DIR/app/build/outputs/apk/debug/app-debug.apk"
readonly TEST_APK="$ROOT_DIR/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk"
readonly CHARTS=(line bar donut gauge radar pie)
readonly THEMES=(light dark)

if ! command -v adb >/dev/null 2>&1; then
    echo "adb를 찾을 수 없습니다. Android SDK platform-tools를 PATH에 추가하세요." >&2
    exit 1
fi

adb_command=(adb)
if [[ -n "${ANDROID_SERIAL:-}" ]]; then
    adb_command+=( -s "$ANDROID_SERIAL" )
else
    device_count="$(adb devices | awk 'NR > 1 && $2 == "device" { count++ } END { print count + 0 }')"
    if [[ "$device_count" -ne 1 ]]; then
        echo "연결된 Android 기기가 정확히 1대여야 합니다. 현재: $device_count" >&2
        echo "여러 기기를 사용할 때는 ANDROID_SERIAL을 지정하세요." >&2
        exit 1
    fi
fi

staging_directory="$(mktemp -d)"
restore_theme() {
    "${adb_command[@]}" shell cmd uimode night auto >/dev/null 2>&1 || true
    rm -rf "$staging_directory"
}
trap restore_theme EXIT

cd "$ROOT_DIR"
./gradlew :app:assembleDebug :app:assembleDebugAndroidTest
"${adb_command[@]}" install -r "$APP_APK"
"${adb_command[@]}" install -r "$TEST_APK"
"${adb_command[@]}" shell rm -rf "$DEVICE_DIRECTORY"

for theme in "${THEMES[@]}"; do
    if [[ "$theme" == "dark" ]]; then
        "${adb_command[@]}" shell cmd uimode night yes
    else
        "${adb_command[@]}" shell cmd uimode night no
    fi
    "${adb_command[@]}" shell am force-stop "$PACKAGE_NAME"
    "${adb_command[@]}" shell am instrument -w \
        -e recordReadmeScreenshots true \
        -e screenshotTheme "$theme" \
        -e class "$TEST_CLASS" \
        "$TEST_RUNNER"
done

"${adb_command[@]}" pull "$DEVICE_DIRECTORY/." "$staging_directory"
mkdir -p "$SCREENSHOT_DIRECTORY"

for chart in "${CHARTS[@]}"; do
    for theme in "${THEMES[@]}"; do
        filename="$chart-chart-$theme.png"
        source_file="$staging_directory/$filename"
        if [[ ! -s "$source_file" ]]; then
            echo "생성된 스크린샷을 찾을 수 없습니다: $filename" >&2
            exit 1
        fi
        cp "$source_file" "$SCREENSHOT_DIRECTORY/$filename"
    done
done

echo "README 스크린샷 12장을 갱신했습니다: $SCREENSHOT_DIRECTORY"
