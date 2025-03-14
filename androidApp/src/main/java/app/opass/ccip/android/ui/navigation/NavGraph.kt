/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.navigation

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
import app.opass.ccip.android.ui.screens.preview.PreviewScreen
import app.opass.ccip.android.ui.screens.schedule.ScheduleScreen
import app.opass.ccip.android.ui.screens.session.SessionScreen
import app.opass.ccip.android.ui.screens.ticket.TicketScreen

/**
 * Navigation graph for compose screens
 * @param navHostController [NavHostController] to navigate with compose
 * @param startDestination Starting destination for the activity/app
 */
@Composable
fun NavGraph(navHostController: NavHostController, startDestination: Screen) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        composable<Screen.Preview> {
            PreviewScreen(navHostController = navHostController)
        }

        composable<Screen.Event> { backStackEntry ->
            val event = backStackEntry.toRoute<Screen.Event>()
            EventScreen(
                eventId = event.eventId,
                navHostController = navHostController,
                viewModel = backStackEntry.sharedViewModel(navHostController)
            )
        }

        composable<Screen.Schedule> { backStackEntry ->
            val schedule = backStackEntry.toRoute<Screen.Schedule>()
            ScheduleScreen(
                eventId = schedule.eventId,
                navHostController = navHostController,
                viewModel = backStackEntry.sharedViewModel(navHostController)
            )
        }

        composable<Screen.Session> { backStackEntry ->
            val session = backStackEntry.toRoute<Screen.Session>()
            SessionScreen(
                eventId = session.eventId,
                sessionId = session.sessionId,
                navHostController = navHostController
            )
        }

        composable<Screen.Ticket> { backStackEntry ->
            val ticket = backStackEntry.toRoute<Screen.Ticket>()
            TicketScreen(
                eventId = ticket.eventId,
                navHostController = navHostController
            )
        }

        composable<Screen.Announcement> { backStackEntry ->
            val announcement = backStackEntry.toRoute<Screen.Announcement>()
            AnnouncementScreen(
                eventId = announcement.eventId,
                onNavigateUp = { navHostController.navigateUp() }
            )
        }
    }
}

/**
 * Gets viewModel from the parent composable
 */
@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val parentEntry = remember (this) { navController.getBackStackEntry(this.destination.route!!) }
    return hiltViewModel<T>(parentEntry)
}
