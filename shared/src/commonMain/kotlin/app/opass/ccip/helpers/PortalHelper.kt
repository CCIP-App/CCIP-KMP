/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.helpers

import app.opass.ccip.database.OPassDatabaseHelper
import app.opass.ccip.network.PortalClient
import app.opass.ccip.network.models.event.Event
import app.opass.ccip.network.models.eventconfig.EventConfig

/**
 * Helper class to interact with OPass portal
 */
class PortalHelper {

    private val dbHelper = OPassDatabaseHelper()
    private val client = PortalClient()

    /**
     * Fetches list of [Event] from OPass portal
     * @param forceReload Whether to ignore cache, false by default
     */
    suspend fun getEvents(forceReload: Boolean = false): List<Event> {
        val cachedEvents = dbHelper.getAllEvents()
        return if (cachedEvents.isNotEmpty() && !forceReload) {
            cachedEvents
        } else {
            client.getEvents().also { dbHelper.addEvents(it) }
        }
    }

    /**
     * Fetches [EventConfig] for specified id from OPass portal
     * @param eventId ID of the event
     * @param forceReload Whether to ignore cache, false by default
     */
    suspend fun getEventConfig(eventId: String, forceReload: Boolean = false): EventConfig {
        val cachedEventConfig = dbHelper.getEventConfig(eventId)
        return if (cachedEventConfig != null && !forceReload) {
            cachedEventConfig
        } else {
            client.getEventConfig(eventId).also { dbHelper.addEventConfig(it) }
        }
    }
}
