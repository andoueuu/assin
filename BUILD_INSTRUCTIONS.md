# Build Instructions

## Prerequisites

1. **Android Studio**: Latest stable version (Hedgehog or later)
2. **JDK**: Java 17 or higher
3. **Android SDK**: API 34 (Android 14)
4. **Gradle**: 8.0 or higher (wrapper included)

## Building the Project

### Method 1: Using Android Studio

1. Open Android Studio
2. Select "Open an existing project"
3. Navigate to the project directory and select it
4. Wait for Gradle sync to complete
5. Build > Build Bundle(s) / APK(s) > Build APK(s)
6. The APK will be generated in `app/build/outputs/apk/debug/`

### Method 2: Using Command Line

1. Navigate to the project directory
2. Make the Gradle wrapper executable:
   ```bash
   chmod +x gradlew
   ```
3. Build the debug APK:
   ```bash
   ./gradlew assembleDebug
   ```
4. The APK will be generated in `app/build/outputs/apk/debug/`

## Installing the APK

1. Enable "Unknown Sources" in Android Settings > Security
2. Copy the APK to your device
3. Open the APK file to install

## Required Permissions Setup

### 1. Enable Accessibility Service

1. Open the app
2. Go to Settings
3. Tap "Enable Accessibility Service"
4. Find "AssistiveTouch" in the list
5. Toggle the switch to enable
6. Confirm the permission dialog

### 2. Enable Overlay Permission (for Floating Button)

1. Open the app
2. Go to Settings
3. Tap "Enable Overlay Permission"
4. Find "AssistiveTouch" in the list
5. Toggle the switch to enable

## Testing the Application

1. **Create a Gesture**:
   - Tap "Create New Gesture..."
   - Draw your gesture on the screen
   - Save with a name

2. **Assign to Custom Action**:
   - Go to Settings > Custom Actions
   - Select an action type (Single-Tap, Double-Tap, Long Press)
   - Choose your saved gesture

3. **Use the Floating Button**:
   - Enable the floating button
   - Drag it to position
   - Tap or double-tap to trigger

## Troubleshooting

**Gestures not replaying:**
- Ensure the accessibility service is enabled
- Check that no other accessibility services are interfering
- Restart the app after enabling permissions

**Floating button not appearing:**
- Ensure overlay permission is granted
- Check if the app is being killed by battery optimization
- Add the app to the "Protected Apps" list (on some OEM devices)

**App crashes on startup:**
- Ensure you're using the latest version of Android Studio
- Clean and rebuild the project: Build > Clean Project, then Build > Rebuild Project
