# AssistiveTouch Gesture Recorder

A native Android application that recreates Apple's AssistiveTouch "Create New Gesture" feature.

## Features

- **Gesture Recording**: Record custom touchscreen gestures with timing and coordinates
- **Custom Actions**: Assign gestures to Single-Tap, Double-Tap, and Long Press
- **Floating Button**: Draggable overlay button that works over other apps
- **Local Storage**: Gestures stored locally using Room Database
- **Modern UI**: Jetpack Compose with iOS-inspired design

## Technical Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Database**: Room
- **Dependency Injection**: Hilt
- **Architecture**: MVVM

## Building the Project

### Prerequisites

1. Android Studio Hedgehog or later
2. JDK 17 or higher
3. Android SDK 34

### Steps

1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Build > Build Bundle(s) / APK(s) > Build APK(s)
4. The APK will be generated in `app/build/outputs/apk/debug/`

## Installation

1. Enable "Unknown Sources" in Android Settings
2. Copy the APK to your device
3. Open the APK file to install

## Required Permissions

1. **Accessibility Service**: Required for gesture replay
   - Settings > Accessibility > AssistiveTouch > Enable

2. **Overlay Permission**: Required for floating button
   - Settings > Apps > Special Access > Draw over other apps

## Usage

1. **Create a Gesture**:
   - Open the app
   - Tap "Create New Gesture..."
   - Draw your gesture on the screen
   - Tap Save and give it a name

2. **Assign to Custom Action**:
   - Go to Settings > Custom Actions
   - Select Single-Tap, Double-Tap, or Long Press
   - Choose your saved gesture

3. **Use the Floating Button**:
   - Enable the floating button from the app
   - Drag it to your preferred position
   - Tap or double-tap based on your configuration

## Limitations

- Gestures can only be recorded within the app (Android security restriction)
- Replay works in other apps via Accessibility Service
- One gesture at a time (no simultaneous strokes)

## License

This project is for educational purposes only.
