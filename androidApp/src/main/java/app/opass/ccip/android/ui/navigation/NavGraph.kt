/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import app.opass.ccip.android.ui.screens.announcement.AnnouncementScreen
import app.opass.ccip.android.ui.screens.event.EventScreen
import app.opass.ccip.android.ui.screens.eventpreview.EventPreviewScreen
import app.opass.ccip.android.ui.screens.schedule.ScheduleScreen
import app.opass.ccip.android.ui.screens.session.SessionScreen
import app.opass.ccip.android.ui.screens.ticket.TicketScreen

@Composable
fun SetupNavGraph(navHostController: NavHostController, startDestination: Screen) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable<Screen.EventPreview> {
            Screen.EventPreview.EventPreviewScreen(navHostController)
        }

        composable<Screen.Event> { backStackEntry ->
            backStackEntry.toRoute<Screen.Event>().EventScreen(
                navHostController,
                backStackEntry.sharedViewModel(navHostController)
            )
        }

        composable<Screen.Schedule> { backStackEntry ->
            backStackEntry.toRoute<Screen.Schedule>().ScheduleScreen(
                navHostController,
                backStackEntry.sharedViewModel(navHostController)
            )
        }

        composable<Screen.Session> { backStackEntry ->
            backStackEntry.toRoute<Screen.Session>().SessionScreen(navHostController)
        }

        composable<Screen.Ticket> { backStackEntry ->
            backStackEntry.toRoute<Screen.Ticket>().TicketScreen(navHostController)
        }

        composable<Screen.Announcement> { backStackEntry ->
            backStackEntry.toRoute<Screen.Announcement>().AnnouncementScreen(navHostController)
        }
    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val parentEntry = remember (this) { navController.getBackStackEntry(this.destination.route!!) }
    return hiltViewModel<T>(parentEntry)
}
