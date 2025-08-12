/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Destination (Screen) for navigation in compose
 */
@Serializable
sealed class Screen: NavKey {

    @Serializable
    data object Preview : Screen()

    @Serializable
    data class Event(val eventId: String) : Screen()

    @Serializable
    data class Schedule(val eventId: String) : Screen()

    @Serializable
    data class Session(val eventId: String, val sessionId: String) : Screen()

    @Serializable
    data class RequestTicket(val eventId: String) : Screen()

    @Serializable
    data class ScanTicket(val eventId: String) : Screen()

    @Serializable
    data class ShowTicket(val eventId: String, val token: String) : Screen()

    @Serializable
    data class Announcement(val eventId: String, val token: String? = null) : Screen()
}
