object Versions {
    const val kotlin = "1.9.22"
    const val androidGradlePlugin = "8.1.0"
    const val hilt = "2.48"
    const val compose = "1.5.4"
    const val composeCompiler = "1.5.8"
    const val coreKtx = "1.12.0"
    const val lifecycle = "2.6.2"
    const val activityCompose = "1.8.0"
    const val material3 = "1.1.2"
    const val navigation = "2.7.4"
    const val coroutines = "1.7.3"
    const val room = "2.6.0"
    const val junit = "4.13.2"
    const val mockk = "1.13.8"
    const val turbine = "1.0.0"
    const val androidJunit = "1.1.5"
    const val espresso = "3.5.1"
    const val archCore = "2.2.0"
    const val lifecycleViewModel = "2.6.2"
    const val detekt = "1.23.5"
    const val ktlint = "11.6.1"
}

object Deps {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    
    const val detektFormatting = "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.detekt}"
    
    object Plugins {
        const val detekt = "io.gitlab.arturbosch.detekt"
        const val ktlint = "org.jlleitschuh.gradle.ktlint"
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
        const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
        const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleViewModel}"
        const val archCore = "androidx.arch.core:core-testing:${Versions.archCore}"
    }

    object Compose {
        const val ui = "androidx.compose.ui:ui:${Versions.compose}"
        const val uiGraphics = "androidx.compose.ui:ui-graphics:${Versions.compose}"
        const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
        const val material3 = "androidx.compose.material3:material3:${Versions.material3}"
        // Add missing Compose dependency
        const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:${Versions.compose}"
    }

    object Navigation {
        const val compose = "androidx.navigation:navigation-compose:${Versions.navigation}"
    }

    object Hilt {
        const val android = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val compiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
        const val navigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0"
    }

    object Coroutines {
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    }

    object Room {
        const val runtime = "androidx.room:room-runtime:${Versions.room}"
        const val ktx = "androidx.room:room-ktx:${Versions.room}"
        const val compiler = "androidx.room:room-compiler:${Versions.room}"
    }

    object Test {
        const val junit = "junit:junit:${Versions.junit}"
        const val mockk = "io.mockk:mockk:${Versions.mockk}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
        const val turbine = "app.cash.turbine:turbine:${Versions.turbine}"
        const val androidJunit = "androidx.test.ext:junit:${Versions.androidJunit}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    }
}