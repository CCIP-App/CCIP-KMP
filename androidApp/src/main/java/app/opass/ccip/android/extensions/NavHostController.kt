/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.extensions

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import app.opass.ccip.android.ui.navigation.Screen

/**
 * Overloaded navigate method that allows popping
 * @param screen Destination [Screen]
 * @param popUpToRoute Route of the screen to popUpTo, defaults to startDestination
 * @param isInclusive Whether the popUpToRoute should also be popped up, defaults to false
 */
fun NavHostController.navigate(
    screen: Screen,
    popUpToRoute: String = graph.findStartDestination().route!!,
    isInclusive: Boolean = false
) {
    navigate(screen) {
        popUpTo(popUpToRoute) {
            inclusive = isInclusive
        }
        launchSingleTop = true
    }
}
