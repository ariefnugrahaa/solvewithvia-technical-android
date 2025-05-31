plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("jacoco")
}

configureAndroidModule()
configureCompose()
configureHilt()

android {
    namespace = AndroidConfig.applicationId
    defaultConfig {
        applicationId = AndroidConfig.applicationId
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":feature:request"))
    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.lifecycleRuntime)
    implementation(Deps.AndroidX.activityCompose)
    implementation(Deps.Compose.ui)
    implementation(Deps.Compose.uiGraphics)
    implementation(Deps.Compose.uiTooling)
    implementation(Deps.Compose.uiToolingPreview)
    implementation(Deps.Compose.material3)
    implementation(Deps.Navigation.compose)
    implementation(Deps.Coroutines.android)
    testImplementation(Deps.Test.junit)
    testImplementation(Deps.Test.mockk)
    testImplementation(Deps.Test.coroutines)
    testImplementation(Deps.Test.turbine)
    androidTestImplementation(Deps.Test.androidJunit)
    androidTestImplementation(Deps.Test.espresso)
    detektPlugins(Deps.detektFormatting)
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*"
    )

    val debugTree = fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug").get().asFile) {
        exclude(fileFilter)
    }

    sourceDirectories.setFrom(files("${project.projectDir}/src/main/java"))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(fileTree(layout.buildDirectory) {
        include("/jacoco/testDebugUnitTest.exec")
    })
}