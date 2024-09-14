/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.helpers

import app.opass.ccip.database.OPassDatabaseHelper
import app.opass.ccip.network.PortalClient
import app.opass.ccip.network.models.event.Event
import app.opass.ccip.network.models.eventconfig.EventConfig
import app.opass.ccip.network.models.eventconfig.FeatureType
import app.opass.ccip.network.models.schedule.Schedule
import app.opass.ccip.network.models.schedule.Session
import app.opass.ccip.network.models.schedule.Speaker

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

    /**
     * Fetches [Schedule] for specified id from Event's website
     * @param eventId ID of the event
     * @param forceReload Whether to ignore cache, false by default
     * @return null if eventConfig hasn't been cached yet or event doesn't have a schedule; Schedule otherwise
     */
    suspend fun getSchedule(eventId: String, forceReload: Boolean = false): Schedule? {
        val eventConfig = dbHelper.getEventConfig(eventId) ?: return null
        val feat = eventConfig.features.find { f -> f.type == FeatureType.SCHEDULE } ?: return null

        val cachedSchedule = Schedule(
            rooms = dbHelper.getRooms(eventId),
            tags = dbHelper.getTags(eventId),
            sessionTypes = dbHelper.getSessionTypes(eventId),
            speakers = dbHelper.getSpeakers(eventId),
            sessions = dbHelper.getSessions(eventId)
        )
        return if (cachedSchedule.sessions.isNotEmpty() && !forceReload) {
            cachedSchedule
        } else {
            client.getEventSchedule(feat.url!!).also {
                dbHelper.apply {
                    addRooms(eventConfig.id, it.rooms)
                    addTags(eventConfig.id, it.tags)
                    addSessionTypes(eventConfig.id, it.sessionTypes)
                    addSpeakers(eventConfig.id, it.speakers)
                    addSessions(eventConfig.id, it.sessions)
                }
            }
        }
    }

    /**
     * Fetches [Schedule] for specified id from Event's website
     * @param eventId ID of the event
     * @param speakerId ID of the speaker
     * @return null if schedule hasn't been cached yet; speaker otherwise
     */
    suspend fun getSpeaker(eventId: String, speakerId: String): Speaker? {
        return dbHelper.getSpeaker(eventId, speakerId)
    }

    /**
     * Fetches [Schedule] for specified id from Event's website
     * @param eventId ID of the event
     * @param sessionId ID of the session
     * @return null if schedule hasn't been cached yet; session otherwise
     */
    suspend fun getSession(eventId: String, sessionId: String): Session? {
        return dbHelper.getSession(eventId, sessionId)
    }
}
