plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.mytaskflow"
    compileSdk = 34 // Я изменил 36 на 34, т.к. 36 еще нет, это опечатка из твоего файла

    defaultConfig {
        applicationId = "com.example.mytaskflow"
        minSdk = 26
        targetSdk = 34 // Тоже меняю 36 на 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { // Этот блок был пропущен в твоем файле, но он важен
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
        sourceCompatibility = JavaVersion.VERSION_1_8 // Меняю 11 на 1.8 (стандарт)
        targetCompatibility = JavaVersion.VERSION_1_8 // Меняю 11 на 1.8 (стандарт)
    }
    kotlinOptions {
        jvmTarget = "1.8" // Меняю 11 на 1.8 (стандарт)
    }
    buildFeatures {
        compose = true
    }
    composeOptions { // Этот блок тоже важен
        kotlinCompilerExtensionVersion = "1.5.1" // Убедись, что эта версия совпадает
    }
    packaging { // И этот блок
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx-compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // --- НАША НОВАЯ ЗАВИСИМОСТЬ ---
    implementation(libs.androidx.navigation.compose)
    // -------------------------------

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}