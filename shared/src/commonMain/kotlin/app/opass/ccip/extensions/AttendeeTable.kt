/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.extensions

import app.opass.ccip.database.AttendeeTable
import app.opass.ccip.network.models.fastpass.Attendee
import app.opass.ccip.network.models.fastpass.Scenario
import kotlinx.serialization.json.Json

internal fun AttendeeTable.toAttendee(): Attendee {
    return Attendee(
        attr = Json.decodeFromString<Map<String, String>>(this.attr),
        eventId = this.eventId,
        firstUse = this.firstUse,
        role = this.role,
        scenarios = Json.decodeFromString<List<Scenario>>(this.scenarios),
        token = this.token,
        userId = this.userId,
    )
}
