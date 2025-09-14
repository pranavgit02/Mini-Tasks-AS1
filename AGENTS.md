# Repository Guidelines

## Project Structure & Module Organization
- Single-module Android app in `app/`.
- Source: `app/src/main/java`, resources: `app/src/main/res`, manifest: `app/src/main/AndroidManifest.xml`.
- Tests: unit in `app/src/test`, instrumented in `app/src/androidTest`.
- Versions managed via `gradle/libs.versions.toml`; root Gradle config in `build.gradle.kts` and `settings.gradle.kts`.

## Build, Test, and Development Commands
- Build debug APK: `./gradlew :app:assembleDebug` (outputs in `app/build/outputs/apk/`).
- Run unit tests: `./gradlew :app:testDebugUnitTest`.
- Run instrumented tests (device/emulator required): `./gradlew :app:connectedDebugAndroidTest`.
- Lint checks: `./gradlew :app:lintDebug`.
- Install debug on a device: `./gradlew :app:installDebug`.

## Coding Style & Naming Conventions
- Kotlin, Java 17 toolchain; `compileSdk=36`, `minSdk=24`.
- Follow Kotlin idiomatic style: 4-space indent, 120-char soft limit, no wildcard imports.
- Class/file names in PascalCase (`MainActivity.kt`), package names lowercase (`com.example.streamchatdemo`).
- Compose: `@Composable` functions in PascalCase; preview functions suffixed with `Preview`.
- Resources: snake_case (e.g., `activity_main.xml`, `ic_send.xml`, `color_primary`); string keys scoped by feature (`chat_send_button_label`).

## Testing Guidelines
- Unit tests: JUnit4 in `app/src/test` (name files `ClassNameTest.kt`).
- UI/instrumented tests: Espresso/Compose test APIs in `app/src/androidTest` (name `FeatureNameTest.kt`).
- Prefer small, deterministic tests; mock Android APIs where needed. Run locally via commands above or Android Studio.

## Commit & Pull Request Guidelines
- Commits: concise, imperative summary; prefer Conventional Commits (e.g., `feat: add message list screen`).
- Include context in body: what/why, not just how; reference issues.
- PRs: clear description, screenshots for UI changes, steps to test, and any migration notes. Keep PRs focused and reviewable.

## Security & Configuration Tips
- Do not commit secrets or `local.properties`. Use Gradle properties or environment variables for tokens.
- Update versions via `libs.versions.toml` and test both unit and instrumented suites after dependency bumps.

## Agent-Specific Notes
- Keep changes minimal and localized; avoid altering the version catalog or Gradle plugins without justification. Update this doc when build/test flows change.
