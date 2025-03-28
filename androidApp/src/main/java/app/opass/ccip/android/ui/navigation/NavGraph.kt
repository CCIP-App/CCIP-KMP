/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import app.opass.ccip.android.ui.extensions.navigate
import app.opass.ccip.android.ui.extensions.sharedViewModel
import app.opass.ccip.android.ui.screens.announcement.AnnouncementScreen
import app.opass.ccip.android.ui.screens.event.EventScreen
import app.opass.ccip.android.ui.screens.preview.PreviewScreen
import app.opass.ccip.android.ui.screens.schedule.ScheduleScreen
import app.opass.ccip.android.ui.screens.session.SessionScreen
import app.opass.ccip.android.ui.screens.ticket.request.RequestTicketScreen
import app.opass.ccip.android.ui.screens.ticket.scan.ScanTicketScreen
import app.opass.ccip.android.ui.screens.ticket.show.ShowTicketScreen

/**
 * Navigation graph for compose screens
 * @param navHostController [NavHostController] to navigate with compose
 * @param startDestination Starting destination for the activity/app
 */
@Composable
fun NavGraph(navHostController: NavHostController, startDestination: Screen) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        composable<Screen.Preview> {
            PreviewScreen(
                onNavigateToEvent = { eventId ->
                    navHostController.navigate(
                        screen = Screen.Event(eventId),
                        isInclusive = true
                    )
                },
                onNavigateUp = if (navHostController.previousBackStackEntry != null) {
                    { navHostController.navigateUp() }
                } else {
                    null
                }
            )
        }

        composable<Screen.Event> { backStackEntry ->
            val event = backStackEntry.toRoute<Screen.Event>()
            EventScreen(
                eventId = event.eventId,
                viewModel = backStackEntry.sharedViewModel(navHostController),
                onNavigateUp = { navHostController.navigate(Screen.Preview) },
                onNavigateToSchedule = {
                    navHostController.navigate(
                        Screen.Schedule(event.eventId)
                    )
                },
                onNavigateToTicket = { token ->
                    if (token != null) {
                        navHostController.navigate(
                            Screen.ShowTicket(event.eventId, token)
                        )
                    } else {
                        navHostController.navigate(
                            Screen.RequestTicket(event.eventId)
                        )
                    }
                },
                onNavigateToAnnouncement = { token ->
                    navHostController.navigate(
                        Screen.Announcement(event.eventId, token)
                    )
                }
            )
        }

        composable<Screen.Schedule> { backStackEntry ->
            val schedule = backStackEntry.toRoute<Screen.Schedule>()
            ScheduleScreen(
                eventId = schedule.eventId,
                viewModel = backStackEntry.sharedViewModel(navHostController),
                onNavigateUp = { navHostController.navigateUp() },
                onNavigateToSession = { sessionId ->
                    navHostController.navigate(
                        Screen.Session(schedule.eventId, sessionId)
                    )
                }
            )
        }

        composable<Screen.Session> { backStackEntry ->
            val session = backStackEntry.toRoute<Screen.Session>()
            SessionScreen(
                eventId = session.eventId,
                sessionId = session.sessionId,
                onNavigateUp = { navHostController.navigateUp() }
            )
        }

        composable<Screen.RequestTicket> { backStackEntry ->
            val ticket = backStackEntry.toRoute<Screen.RequestTicket>()
            RequestTicketScreen(
                eventId = ticket.eventId,
                onNavigateUp = { navHostController.navigateUp() },
                onNavigateToShowTicket = { token ->
                    navHostController.navigate(
                        screen = Screen.ShowTicket(ticket.eventId, token)
                    )
                },
                onNavigateToScanTicket = {
                    navHostController.navigate(
                        Screen.ScanTicket(ticket.eventId)
                    )
                }
            )
        }

        composable<Screen.ScanTicket> { backStackEntry ->
            val scan = backStackEntry.toRoute<Screen.ScanTicket>()
            ScanTicketScreen(
                eventId = scan.eventId,
                onNavigateUp = { navHostController.navigateUp() },
                onNavigateToShowTicket = { token ->
                    navHostController.navigate(
                        screen = Screen.ShowTicket(scan.eventId, token)
                    )
                }
            )
        }

        composable<Screen.ShowTicket> { backStackEntry ->
            val ticket = backStackEntry.toRoute<Screen.ShowTicket>()
            ShowTicketScreen(
                eventId = ticket.eventId,
                token = ticket.token,
                onNavigateUp = { navHostController.navigateUp() },
                onNavigateToRequestTicket = {
                    navHostController.navigate(
                        screen = Screen.RequestTicket(ticket.eventId)
                    )
                }
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
