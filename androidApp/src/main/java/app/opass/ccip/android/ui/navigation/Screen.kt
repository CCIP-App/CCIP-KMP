/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import app.opass.ccip.android.R
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(@StringRes val title: Int, @DrawableRes val icon: Int) {

    @Serializable
    data object EventPreview : Screen(
        title = R.string.select_event,
        icon = R.drawable.ic_calendar
    )

    @Serializable
    data class Event(val id: String) : Screen(
        title = R.string.event,
        icon = R.drawable.ic_event
    )

    @Serializable
    data class Schedule(val eventId: String) : Screen(
        title = R.string.schedule,
        icon = R.drawable.ic_schedule
    )

    @Serializable
    data class Session(val eventId: String, val sessionId: String) : Screen(
        title = R.string.session,
        icon = R.drawable.ic_podium
    )

    @Serializable
    data class Ticket(val eventId: String) : Screen(
        title = R.string.ticket,
        icon = R.drawable.ic_ticket
    )

    @Serializable
    data class Announcement(val eventId: String, val token: String? = null) : Screen(
        title = R.string.announcement,
        icon = R.drawable.ic_announcement
    )
}
