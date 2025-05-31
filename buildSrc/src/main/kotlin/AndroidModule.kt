import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.configureAndroidModule() {
    extensions.findByName("android")?.let { android ->
        if (android is BaseExtension) {
            android.apply {
                compileSdkVersion(AndroidConfig.compileSdk)

                defaultConfig {
                    minSdk = AndroidConfig.minSdk
                    targetSdk = AndroidConfig.targetSdk
                    versionCode = AndroidConfig.versionCode
                    versionName = AndroidConfig.versionName

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                buildTypes {
                    getByName("release") {
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

                tasks.withType<KotlinCompile> {
                    kotlinOptions {
                        jvmTarget = "17"
                    }
                }
            }
        }
    }
}

fun Project.configureCompose() {
    extensions.findByName("android")?.let { android ->
        if (android is CommonExtension<*, *, *, *, *>) {
            android.apply {
                buildFeatures {
                    compose = true
                }

                composeOptions {
                    kotlinCompilerExtensionVersion = Versions.composeCompiler
                }
            }
        }
    }
}

fun Project.configureHilt() {
    dependencies {
        "implementation"(Deps.Hilt.android)
        "kapt"(Deps.Hilt.compiler)
        "implementation"(Deps.Hilt.navigationCompose)
    }
} 