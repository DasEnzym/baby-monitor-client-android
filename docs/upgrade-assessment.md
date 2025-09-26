# Dependency Upgrade Assessment

## Current build tooling
- The project still relies on Android Gradle Plugin 3.5.3 with Gradle 5.4.1 and Kotlin 1.3.61. These versions are defined in the root `build.gradle` and the Gradle wrapper configuration.【F:build.gradle†L5-L22】【F:gradle/wrapper/gradle-wrapper.properties†L1-L7】
- The `app` module continues to use the legacy `kotlin-android-extensions` plugin to access synthetic view bindings.【F:app/build.gradle†L3-L6】

## Why the requested dependency bumps fail today
- Jetpack Lifecycle 2.7.0, Navigation 2.7.x and Room 2.6.x are compiled with Kotlin 1.9 metadata. Kotlin 1.3.61 (the version bundled with AGP 3.5.3) cannot read this metadata, so the compiler aborts with `Unsupported [Kotlin] metadata version` errors. Raising the Kotlin compiler to >= 1.9 requires upgrading the Android Gradle Plugin and Gradle wrapper first, which is a large, cross-cutting change.
- Android Gradle Plugin 3.5.3 does not support the modern Crashlytics and Firebase Performance Gradle plugins that ship with the Firebase BOM. Fabric's deprecated Crashlytics plugin is still applied through the `io.fabric` Gradle plugin in the `app` module.【F:app/build.gradle†L7-L11】 Migrating to the Firebase Crashlytics plugin is blocked until the build system itself is updated.
- The project relies extensively on Kotlin synthetic view bindings (`kotlinx.android.synthetic`).【F:app/src/main/kotlin/co/netguru/baby/monitor/client/feature/client/home/ClientHomeActivity.kt†L25-L28】 Kotlin removed this feature in 1.8+, so upgrading the Kotlin compiler would require replacing every synthetic usage with View Binding or `findViewById` across the codebase.
- Several legacy libraries (e.g. TensorFlow 1.13, EasyImage 1.3.1, RxJava 2) were chosen because they integrate with the existing reactive + synthetic architecture. Replacing them with their modern counterparts (TensorFlow Lite, EasyImage 3.x, RxJava 3) demands API migrations throughout the app, not just dependency bumps.
- Running Gradle tasks on modern JDKs already fails with the current toolchain (`Gradle 5.4.1` + `JDK 21` results in `Could not initialize class org.codehaus.groovy.runtime.InvokerHelper`). Aligning the tooling would require standardising on a supported JDK (e.g. 11) and upgrading Gradle together with AGP.

## Suggested migration path
1. Modernise the build system: update the Gradle wrapper to 8.x, bump AGP to 8.x, and raise Kotlin to ≥ 1.9.20. This must include removing `kotlin-android-extensions` in favour of View Binding.
2. After the build infrastructure is current, incrementally migrate feature modules away from deprecated libraries (RxJava 3 / Kotlin Coroutines, Firebase BOM + Crashlytics plugin, TensorFlow Lite, updated EasyImage APIs, etc.).
3. Only once the above structural changes land can the requested dependency versions (Lifecycle ≥ 2.7, Room ≥ 2.6, Navigation ≥ 2.7, Retrofit 2.9, OkHttp 4.12, Firebase BOM, etc.) be introduced without breaking the build.

Because these prerequisite migrations are extensive and cross-cutting, the dependency refresh cannot be delivered safely in a single step without first modernising the build stack.
