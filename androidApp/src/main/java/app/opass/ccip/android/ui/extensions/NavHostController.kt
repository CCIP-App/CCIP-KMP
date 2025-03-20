/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.extensions

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import app.opass.ccip.android.ui.navigation.Screen

fun NavHostController.popBackToScreen(screen: Screen, popBackToStart: Boolean = false) {
    navigate(screen) {
        if (popBackToStart) {
            popUpTo(graph.findStartDestination().id) { inclusive = true }
        } else {
            popUpTo(previousBackStackEntry!!.id) { inclusive = true }
        }
        launchSingleTop = true
    }
}
