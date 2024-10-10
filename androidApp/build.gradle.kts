/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.googleKSP)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.googleHiltAndroid)
}

android {
    namespace = "app.opass.ccip.android"
    compileSdk = rootProject.extra["androidTargetSdk"] as Int
    defaultConfig {
        applicationId = "app.opass.ccip"
        minSdk = rootProject.extra["androidMinSdk"] as Int
        targetSdk = rootProject.extra["androidTargetSdk"] as Int
        versionCode = 56
        versionName = "3.6.0"

        manifestPlaceholders["manifestApplicationId"] = "$applicationId"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
    lint {
        lintConfig = file("lint.xml")
    }
    androidResources {
        generateLocaleConfig = true
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.adaptive.android)
    debugImplementation(libs.compose.ui.tooling)

    // Browser
    implementation(libs.androidx.browser)

    // Coil
    implementation(libs.coil.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)

    // Material
    implementation(libs.google.material.components)

    // Markdown
    implementation(libs.mikepenz.markdown)

    // Hilt
    ksp(libs.google.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation)
    implementation(libs.google.hilt.android.core)

    // ZXing/Camera (QR)
    implementation(libs.androidx.camera)
    implementation(libs.zxing.core)
    implementation(libs.zxing.cpp)
}
