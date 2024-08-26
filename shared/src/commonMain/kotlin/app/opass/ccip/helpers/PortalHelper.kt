/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.helpers

import app.opass.ccip.database.OPassDatabaseHelper
import app.opass.ccip.network.PortalClient
import app.opass.ccip.network.models.event.Event

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
}
