# HelpDesk Lite — ESTIAM E5 M5

HelpDesk Lite is an Android application for tracking campus IT support tickets. Open this directory, which contains `settings.gradle.kts`, as an Android Studio project.

## Setup

Select Java 17 as the Gradle JDK in Android Studio. For PowerShell, set `JAVA_HOME` to your Java 17 installation and make sure `java` and the Android SDK’s `platform-tools` are on PATH. Install Android SDK Platform 35 through Android Studio if it is missing. Allow Gradle to fetch dependencies on the first sync. Use the built-in Run action with the `app` run configuration to launch the app.

The wrapper uses Gradle 8.13, with AGP 8.11.1, Kotlin 2.2.20 and Java/Kotlin 17 targets. The app uses Kotlin, XML and ViewBinding, Room 2.7.2, JUnit 4.13.2, and Espresso Core and Contrib 3.7.0. The minimum device API is 26. Compilation and targeting use API 35.

## Windows PowerShell

```powershell
java -version
adb devices
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:connectedDebugAndroidTest
```

`adb devices` must report the emulator or connected device in the `device` state. Wait for an `offline` emulator to boot. For an `unauthorized` device, unlock it and accept the USB debugging prompt. Launch and unlock the emulator/device before running Espresso.

| Output | Location |
| --- | --- |
| Debug APK | `app/build/outputs/apk/debug/app-debug.apk` |
| Local JUnit HTML | `app/build/reports/tests/testDebugUnitTest/index.html` |
| Local JUnit XML | `app/build/test-results/testDebugUnitTest/` |
| Instrumented HTML | `app/build/reports/androidTests/connected/debug/index.html` |
| Instrumented XML | `app/build/outputs/androidTest-results/connected/debug/` |

## Start the assessment

Read [Assessment Instructions](Assessment_Instructions_EN.md). The starter launches with an empty catalogue. Supplied exercises have deliberate assertion failures until their TODOs are complete. The Exercise 5 placeholder is ignored until you implement it and remove `@Ignore`.
