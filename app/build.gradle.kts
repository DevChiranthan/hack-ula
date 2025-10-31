plugins {
    // These aliases come from your libs.versions.toml file
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.runanywhere.startup_hackathon20"
    // --- FIX: Set to 34, the stable SDK for Android 14 ---
    compileSdk = 34

    defaultConfig {
        applicationId = "com.runanywhere.startup_hackathon20"
        minSdk = 24
        // --- FIX: Set to 34, the stable SDK for Android 14 ---
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        // --- FIX: Use Java 1.8, the standard for Android ---
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        // --- FIX: Match jvmTarget with Java version ---
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    // --- FIX: Add explicit Compose compiler options ---
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Check for latest compatible version
    }
    // --- FIX: Add packaging options to prevent build conflicts ---
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // RunAnywhere SDK - Local AARs from GitHub Release v0.1.3-alpha
    // Core SDK (4.01MB)
    implementation(files("libs/RunAnywhereKotlinSDK-release.aar"))
    // LLM Module (2.12MB) - includes llama.cpp with 7 ARM64 CPU variants
    implementation(files("libs/runanywhere-llm-llamacpp-release.aar"))

    // Required SDK dependencies (transitive dependencies from AARs)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0") // Use a more common version
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

    // Ktor for networking (required by SDK)
    implementation("io.ktor:ktor-client-core:2.3.11") // Use a more common version block
    implementation("io.ktor:ktor-client-okhttp:2.3.11")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.11")
    implementation("io.ktor:ktor-client-logging:2.3.11")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.11")

    // OkHttp (required by SDK)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Retrofit (required by SDK)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Gson (required by SDK)
    implementation("com.google.code.gson:gson:2.11.0")

    // Okio (required by SDK)
    implementation("com.squareup.okio:okio:3.9.0") // Use a more common version

    // AndroidX WorkManager (required by SDK)
    implementation("androidx.work:work-runtime-ktx:2.9.0") // Use a more common version

    // AndroidX Room (required by SDK)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    // implementation("androidx.room:room-compiler:2.6.1") // You might need this too
    // ksp("androidx.room:room-compiler:2.6.1") // If you use KSP

    // AndroidX Security (required by SDK)
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // --- Standard app dependencies (FIXED: Replaced all 'libs' aliases) ---
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")

    // -- Compose BOM (Bill of Materials) --
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // -- ViewModels for Compose --
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

    // --- CRITICAL FIX: Added missing Navigation dependency ---
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // -- Test Dependencies (FIXED: Replaced all 'libs' aliases) --
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}