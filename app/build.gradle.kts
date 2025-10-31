plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Исправление для KSP
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.mytaskflow"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mytaskflow"

        // Исправление для <adaptive-icon>
        minSdk = 26

        targetSdk = 34
        versionCode = 1
        versionName = "1.0"


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        // Исправление для Java 17
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        // Исправление для Java 17
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        // Эта строка из твоего репозитория - правильная
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Эти зависимости из твоего репозитория - правильные
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    // --- ИСПРАВЛЕНИЕ (UI) ---
    // Используем правильный alias из libs.versions.toml
    implementation(libs.androidx.compose.ui)
    // --- КОНЕЦ ИСПРАВЛЕНИЯ ---

    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // Исправление для 'material3'
    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // Поддержка Coroutines
    ksp(libs.androidx.room.compiler)      // Аннотации KSP

    implementation(libs.androidx.compose.material.icons.extended)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}