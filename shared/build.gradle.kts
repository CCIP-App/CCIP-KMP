/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqldelight)
}

kotlin {
    jvmToolchain(21)

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "Shared"
            isStatic = false
        }
    }

    compilerOptions {
        freeCompilerArgs.addAll("-Xexpect-actual-classes", "-opt-in=kotlin.uuid.ExperimentalUuidApi")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.content.negotiation)
            implementation(libs.ktor.serialization)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.cash.sqldelight.runtime)
        }
        androidMain.dependencies {
            implementation(libs.androidx.startup.runtime)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.cash.sqldelight.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.cash.sqldelight.native)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "app.opass.ccip"
    compileSdk = rootProject.extra["androidTargetSdk"] as Int
    defaultConfig {
        minSdk = rootProject.extra["androidMinSdk"] as Int
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

sqldelight {
    databases {
        create("OPassDatabase") {
            packageName.set("app.opass.ccip.database")
            generateAsync.set(true)
        }
    }
}
