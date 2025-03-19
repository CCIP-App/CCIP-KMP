/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.googleKSP).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.kotlinSerialization).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.sqldelight).apply(false)
    alias(libs.plugins.googleHiltAndroid).apply(false)
}

// Extra properties
val androidMinSdk by extra(21)
val androidTargetSdk by extra(36)
