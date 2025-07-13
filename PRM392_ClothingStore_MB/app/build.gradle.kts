plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.prm392_clothingstore_mb"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.prm392_clothingstore_mb"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("androidx.activity:activity:1.8.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(libs.appcompat.v161)
    implementation(libs.material.v190)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.constraintlayout.v214)
    implementation(libs.cronet.embedded)
    implementation(libs.activity)
}