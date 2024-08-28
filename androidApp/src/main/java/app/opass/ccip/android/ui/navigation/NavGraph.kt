/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import app.opass.ccip.android.MainViewModel
import app.opass.ccip.android.ui.screens.event.EventScreen
import app.opass.ccip.android.ui.screens.eventpreview.EventPreviewScreen

@Composable
fun SetupNavGraph(
    navHostController: NavHostController,
    startDestination: Screen,
    viewModel: MainViewModel
) {

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable<Screen.EventPreview> {
            Screen.EventPreview.EventPreviewScreen(navHostController, viewModel)
        }

        composable<Screen.Event> { backStackEntry ->
            backStackEntry.toRoute<Screen.Event>().EventScreen(navHostController, viewModel)
        }
    }
}
