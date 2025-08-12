/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import app.opass.ccip.android.ui.screens.announcement.AnnouncementScreen
import app.opass.ccip.android.ui.screens.event.EventScreen
import app.opass.ccip.android.ui.screens.preview.PreviewScreen
import app.opass.ccip.android.ui.screens.schedule.ScheduleScreen
import app.opass.ccip.android.ui.screens.session.SessionScreen
import app.opass.ccip.android.ui.screens.ticket.request.RequestTicketScreen
import app.opass.ccip.android.ui.screens.ticket.scan.ScanTicketScreen
import app.opass.ccip.android.ui.screens.ticket.show.ShowTicketScreen

/**
 * Navigation display for compose screens
 * @param startDestination Starting destination for the activity/app
 */
@Composable
fun NavDisplay(startDestination: NavKey) {
    val backstack = rememberNavBackStack(startDestination)

    NavDisplay(
        backStack = backstack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Screen.Preview> {
                PreviewScreen(
                    onNavigateToEvent = { eventId ->
                        backstack.clear()
                        backstack.add(Screen.Event(eventId))
                    },
                    onNavigateUp = if (backstack.size == 1 && backstack.first() == Screen.Preview) {
                        null
                    } else {
                        { backstack.removeLastOrNull() }
                    }
                )
            }

            entry<Screen.Event> { screen ->
                EventScreen(
                    eventId = screen.eventId,
                    onNavigateUp = { backstack.add(Screen.Preview) },
                    onNavigateToSchedule = {
                        backstack.add(Screen.Schedule(screen.eventId))
                    },
                    onNavigateToTicket = { token ->
                        if (token != null) {
                            backstack.add(
                                Screen.ShowTicket(screen.eventId, token)
                            )
                        } else {
                            backstack.add(
                                Screen.RequestTicket(screen.eventId)
                            )
                        }
                    },
                    onNavigateToAnnouncement = { token ->
                        backstack.add(Screen.Announcement(screen.eventId, token))
                    }
                )
            }

            entry<Screen.Schedule> { screen ->
                ScheduleScreen(
                    eventId = screen.eventId,
                    onNavigateUp = { backstack.removeLastOrNull() },
                    onNavigateToSession = { sessionId ->
                        backstack.add(Screen.Session(screen.eventId, sessionId))
                    }
                )
            }

            entry<Screen.Session> { screen ->
                SessionScreen(
                    eventId = screen.eventId,
                    sessionId = screen.sessionId,
                    onNavigateUp = { backstack.removeLastOrNull() }
                )
            }

            entry<Screen.RequestTicket> { screen ->
                RequestTicketScreen(
                    eventId = screen.eventId,
                    onNavigateUp = { backstack.removeLastOrNull() },
                    onNavigateToShowTicket = { token ->
                        backstack.removeLastOrNull()
                        backstack.add(Screen.ShowTicket(screen.eventId, token))
                    },
                    onNavigateToScanTicket = {
                        backstack.add(Screen.ScanTicket(screen.eventId))
                    }
                )
            }

            entry<Screen.ScanTicket> { screen ->
                ScanTicketScreen(
                    eventId = screen.eventId,
                    onNavigateUp = { backstack.removeLastOrNull() },
                    onNavigateToShowTicket = { token ->
                        repeat(2) { backstack.removeLastOrNull() }
                        backstack.add(Screen.ShowTicket(screen.eventId, token))
                    }
                )
            }

            entry<Screen.ShowTicket> { screen ->
                ShowTicketScreen(
                    eventId = screen.eventId,
                    token = screen.token,
                    onNavigateUp = { backstack.removeLastOrNull() },
                    onNavigateToRequestTicket = {
                        backstack.removeLastOrNull()
                        backstack.add(Screen.RequestTicket(screen.eventId))
                    }
                )
            }

            entry<Screen.Announcement> { screen ->
                AnnouncementScreen(
                    eventId = screen.eventId,
                    onNavigateUp = { backstack.removeLastOrNull() }
                )
            }
        }
    )
}
