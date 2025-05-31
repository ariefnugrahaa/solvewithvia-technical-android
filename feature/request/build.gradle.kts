plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("jacoco")
}

configureAndroidModule()
configureCompose()
configureHilt()

android {
    namespace = "com.example.feature.request"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:components"))

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

    debugImplementation(Deps.Compose.uiTooling)
    debugImplementation(Deps.Compose.uiTestManifest)
    
    testImplementation(Deps.AndroidX.archCore)
    testImplementation(Deps.AndroidX.lifecycleViewModel)
    testImplementation(Deps.Test.junit)
    testImplementation(Deps.Test.mockk)
    testImplementation(Deps.Test.coroutines)
    testImplementation(Deps.Test.turbine)

    androidTestImplementation(Deps.Test.androidJunit)
    androidTestImplementation(Deps.Test.espresso)
    
    testImplementation(Deps.Hilt.android)
    kaptTest(Deps.Hilt.compiler)
    
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