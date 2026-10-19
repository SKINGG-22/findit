# FindIt — Android prototype

FindIt is a small native Android starter app for searching public web results related to an image. It uses **user-controlled reverse-image search** rather than facial recognition.

## What works
- Pick a photo from the Android file picker.
- Preview the selected photo.
- Open Android's share sheet with the image, then choose a compatible visual-search app/service if installed (for example, Google Lens when it appears as an option).
- Search Google for a name, username, or public website entered by the user.
- No account, app server, database, analytics, or broad storage permission is used by this prototype.

## Important limitations
- This does not crawl the entire internet or automatically collect every public profile.
- Visual-search apps available in the share sheet depend on the phone and installed apps. Some may not accept shared images directly.
- FindIt does not identify a person from facial features. Similar-image results can be wrong and should not be treated as proof of identity.
- If you share an image with another service, that service may receive and process it under its own privacy policy.

## Run it in Android Studio
1. Install a recent Android Studio version with Android SDK Platform 35.
2. Unzip this project and choose **Open** in Android Studio, selecting the `FindItAndroid` folder.
3. Allow Gradle sync to finish. If prompted, install the Android Gradle Plugin/SDK components.
4. Connect an Android phone with USB debugging enabled, or create an emulator (Android 6.0 / API 23 or later).
5. Press **Run** and select the `app` configuration.

## Build an APK
In Android Studio, use **Build → Build Bundle(s) / APK(s) → Build APK(s)**. The debug APK is normally placed at `app/build/outputs/apk/debug/app-debug.apk`.

## Next steps for a production app
For richer search results, integrate a licensed reverse-image-search provider or an image-search API that explicitly supports this use case. Review provider terms, image retention, consent, and rate limits first. Do not promise exhaustive web coverage or definitive person identification.
