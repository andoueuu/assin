# GitHub Setup Instructions

## Step 1: Create GitHub Account
1. Go to https://github.com
2. Click "Sign up"
3. Create a free account

## Step 2: Create New Repository
1. Click the "+" icon in the top right
2. Select "New repository"
3. Name: `AssistiveTouchGesture`
4. Make it **Private** (recommended)
5. Do NOT initialize with README (we already have one)
6. Click "Create repository"

## Step 3: Upload Files
### Option A: Upload ZIP directly
1. On your repository page, click "uploading an existing file"
2. Drag and drop the `AssistiveTouchGesture-GitHub.zip` file
3. Click "Commit changes"

### Option B: Upload extracted files
1. Extract the ZIP on your computer
2. Upload all files and folders to the repository
3. Make sure `.github/workflows/build.yml` is included

## Step 4: Enable GitHub Actions
1. Go to the "Actions" tab in your repository
2. If prompted, click "I understand my workflows, go ahead and enable them"
3. The build should start automatically

## Step 5: Wait for Build
- The build takes 10-15 minutes
- You'll see a green checkmark when it's done

## Step 6: Download APK
1. Go to the "Actions" tab
2. Click on the completed workflow run
3. Scroll down to "Artifacts"
4. Download `app-debug-apk`
5. Extract the ZIP to get the APK file

## Step 7: Install on Phone
1. Transfer the APK to your phone
2. Enable "Unknown Sources" in Settings
3. Install the APK

## Troubleshooting

### Build fails with "Gradle wrapper not found"
- Make sure `gradlew` file is in the root directory
- Make sure `gradle/wrapper/gradle-wrapper.properties` exists

### Build fails with "SDK location not found"
- The workflow should handle this automatically
- If not, create a file `local.properties` with:
  ```
  sdk.dir=/usr/local/android-sdk
  ```

### Build takes too long
- First build is always slower
- Subsequent builds will be faster due to caching

## Need Help?
- Check the "Actions" tab for error messages
- Make sure all files were uploaded correctly
- Ensure the repository is not empty
