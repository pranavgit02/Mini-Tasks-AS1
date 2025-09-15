plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Hilt
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.example.streamchatdemo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.streamchatdemo"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // Debug
        getByName("debug") {
            // Helpful suffixes so all three variants can live side-by-side on a device
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"

            // BuildConfig flags
            buildConfigField("String", "BUILD_TYPE", "\"debug\"")
            buildConfigField("Boolean", "IS_INTERNAL", "false")
            // You can add more flags here later if needed
            isMinifyEnabled = false
        }

        // Release
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "BUILD_TYPE", "\"release\"")
            buildConfigField("Boolean", "IS_INTERNAL", "false")
        }

        // ✅ New build type: internalRelease
        create("internalRelease") {
            // Start from release settings but make it friendlier for internal testing
            initWith(getByName("release"))
            // Easier to debug crashes/logs for internal users
            isMinifyEnabled = false
            // If you don’t have variant-specific resources, fall back to release
            matchingFallbacks += listOf("release")

            applicationIdSuffix = ".internal"
            versionNameSuffix = "-internal"

            buildConfigField("String", "BUILD_TYPE", "\"internalRelease\"")
            buildConfigField("Boolean", "IS_INTERNAL", "true")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    buildFeatures { compose = true
        buildConfig = true
    }
}

dependencies {
    // Template / Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // XML Material theme resources (needed for Theme.Material3.* in manifest/themes.xml)
    implementation("com.google.android.material:material:1.12.0")

    // ViewModel + Compose + Coroutines
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Hilt DI
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
