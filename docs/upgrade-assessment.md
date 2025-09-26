# Dependency Upgrade Assessment

## Current build tooling
- The project now builds with Android Gradle Plugin 8.4.2 on top of Gradle 8.14.3 and Kotlin 1.9.24, as defined in the root `build.gradle` and the Gradle wrapper configuration.【F:build.gradle†L5-L23】【F:gradle/wrapper/gradle-wrapper.properties†L1-L7】
- View Binding is enabled for the `app` module, replacing the previously used synthetic bindings from `kotlin-android-extensions`.【F:app/build.gradle†L1-L64】
- Firebase Crashlytics and Performance Monitoring already rely on the modern Gradle plugins supplied by Google, so no Fabric-era tooling remains in the buildscript.【F:build.gradle†L15-L22】【F:app/build.gradle†L1-L10】

## Status of the requested dependency bumps
- Lifecycle 2.7.0, Navigation 2.7.7 and Room 2.6.1 are already declared in `buildsystem/dependencies.gradle`, satisfying the versions that previously triggered compiler metadata errors.【F:buildsystem/dependencies.gradle†L1-L53】
- Retrofit 2.11.0, OkHttp 4.12.0 and Firebase BOM 33.4.0 are likewise configured, so the networking and Firebase stacks are aligned with the requested releases.【F:buildsystem/dependencies.gradle†L1-L44】

## Remaining modernization considerations
- The reactive layer still depends on RxJava 2.x (including RxKotlin and RxAndroid), which is no longer maintained. Migrating to RxJava 3 or Kotlin Coroutines would require refactoring observers, schedulers and Room integrations throughout the app.【F:buildsystem/dependencies.gradle†L17-L24】【F:buildsystem/dependencies.gradle†L37-L52】
- Dependency injection continues to use Dagger 2.25.2 and the legacy `AndroidSupportInjectionModule`. Updating to a current Dagger release or adopting Hilt would involve regenerating components and adjusting the existing module graph.【F:buildsystem/dependencies.gradle†L1-L32】【F:app/src/main/kotlin/co/netguru/baby/monitor/client/application/di/ApplicationComponent.kt†L1-L20】
- The codebase still ships with JetBrains Anko helpers, a library that has been deprecated for several years. Replacing the remaining usages (e.g. notification helpers) with standard Android APIs would remove another unmaintained dependency.【F:buildsystem/dependencies.gradle†L6-L20】【F:app/src/main/kotlin/co/netguru/baby/monitor/client/common/NotificationHandler.kt†L1-L40】

## Suggested migration path
1. Plan and execute the reactive layer migration (RxJava 2 → RxJava 3 or Coroutines), ensuring Room, networking and UI observers are updated in tandem.
2. Modernise dependency injection by upgrading to the latest Dagger release or Hilt, removing the reliance on the deprecated Android support injection artifacts.
3. Eliminate Anko by rewriting the remaining helper calls with platform APIs, then perform a dependency sweep to confirm no other abandoned libraries linger.
4. Once the above refactors land, re-run the dependency updates toolchain to verify there are no additional compatibility issues before attempting further library bumps.
