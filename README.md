# Strova

A native Android music utility built with Kotlin, Jetpack Compose, and Material 3 Expressive.

Strova is a personal project focused on making music-link handling and download-queue management feel simple and polished on Android.

**The project is currently an early UI prototype.** Real music-provider integration and downloading have not yet been implemented.

## Current Status

**Phase 1 — UI Foundation**

Implemented:
- Native Android application using Kotlin and Jetpack Compose.
- Material 3 Expressive theming.
- Dynamic system colours on Android 12 and newer.
- Home, Queue, Library, and Settings screens.
- Basic link-entry form and in-memory mock queue interaction.

Not implemented:
- Tidal integration or provider authentication.
- Actual audio downloading.
- Persistent download queue or history.
- Local music playback.

## Tech Stack

| Technology | Details |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Design system | Material 3 Expressive |
| Build system | Gradle Kotlin DSL |
| Minimum SDK | Android 8.0 (API 26) |
| Compile SDK | API 37 |
| Target SDK | API 37 |

Key development versions:
- Android Gradle Plugin: 9.5.1
- Kotlin: 2.2.10
- Compose BOM: 2026.09.00
- Material 3: 1.5.0-rc01

These versions reflect the current development configuration and may change.

## Build Locally

### Requirements
- Android Studio with Android SDK Platform 37.
- A compatible JDK (use the Gradle JDK configured by Android Studio).
- Git.

### Clone

```bash
git clone https://github.com/AnkitKumarDhal/Strova.git
cd Strova
```

### Build a Debug APK

Linux and macOS:

```bash
chmod +x gradlew
./gradlew assembleDebug
```

Windows:

```bat
gradlew.bat assembleDebug
```

The generated APK is located at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Alternatively, open the cloned project in Android Studio, sync Gradle, and run the `app` configuration on an Android device or emulator.

## Project Structure

Application source currently resides under `app/src/main/java/com/solqus/strova`:

```text
com.solqus.strova/
├── MainActivity.kt
└── ui/
    ├── StrovaApp.kt
    └── theme/
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```

## Development Notes

- The queue currently holds a single mock entry in Compose state.
- No actual network requests or downloads are performed.
- The application follows the device's light/dark mode and uses dynamic colours where supported.
- Tidal is the intended primary provider to investigate, subject to available official interfaces, applicable terms, and supported access methods.
- Local audio playback is not currently part of the planned initial scope.

## License

No license has been added yet. All rights remain reserved by the copyright holder until a license is explicitly selected and added.