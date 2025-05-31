plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

configureAndroidModule()
configureCompose()

android {
    namespace = "com.example.core.ui"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.lifecycleRuntime)
    implementation(Deps.AndroidX.activityCompose)
    implementation(Deps.Compose.ui)
    implementation(Deps.Compose.uiGraphics)
    implementation(Deps.Compose.uiTooling)
    implementation(Deps.Compose.uiToolingPreview)
    implementation(Deps.Compose.material3)
    detektPlugins(Deps.detektFormatting)
}