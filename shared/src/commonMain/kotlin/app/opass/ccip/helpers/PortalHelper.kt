/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.helpers

import app.opass.ccip.database.OPassDatabaseHelper
import app.opass.ccip.network.PortalClient
import app.opass.ccip.network.models.common.LocalizedObject
import app.opass.ccip.network.models.event.Event
import app.opass.ccip.network.models.eventconfig.EventConfig
import app.opass.ccip.network.models.eventconfig.FeatureType
import app.opass.ccip.network.models.fastpass.Announcement
import app.opass.ccip.network.models.fastpass.Attendee
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

        val cachedSchedule = dbHelper.getSchedule(eventId)
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
            // Fetch from DB to let the sorting work
            dbHelper.getSchedule(eventId)
        }
    }

    /**
     * Fetches [Announcement] for specified event using given token from event's FastPass feature
     * @param eventId ID of the event
     * @param token Token to identify attendee
     * @param forceReload Whether to ignore cache, false by default
     * @return empty list if announcements haven't been cached yet or token is invalid; Announcements otherwise
     */
    suspend fun getAnnouncements(
        eventId: String,
        token: String? = null,
        forceReload: Boolean = false
    ): List<Announcement> {
        val eventConfig = dbHelper.getEventConfig(eventId) ?: return emptyList()
        val feat = eventConfig.features.find { f -> f.type == FeatureType.FAST_PASS } ?: return emptyList()

        val cachedAnnouncements = dbHelper.getAllAnnouncements(eventId, token)
        return if (cachedAnnouncements.isNotEmpty() && !forceReload) {
            cachedAnnouncements
        } else {
            client.getAnnouncements(feat.url!!, token).also {
                dbHelper.addAnnouncements(eventId, it, token)
            }
        }
    }

    /**
     * Fetches [Attendee] for specified event using given token from event's FastPass feature
     * @param eventId ID of the event
     * @param token Token to identify attendee
     * @param forceReload Whether to ignore cache, false by default
     * @return null if attendee hasn't been cached yet or token is invalid; Attendee otherwise
     */
    suspend fun getAttendee(
        eventId: String,
        token: String,
        forceReload: Boolean = false
    ): Attendee? {
        val eventConfig = dbHelper.getEventConfig(eventId) ?: return null
        val feat = eventConfig.features.find { f -> f.type == FeatureType.FAST_PASS } ?: return null

        val cachedAttendee = dbHelper.getAttendee(eventId, token)
        return if (cachedAttendee != null && !forceReload) {
            cachedAttendee
        } else {
            client.getFastPassStatus(feat.url!!, token).also {
                dbHelper.addAttendee(eventId, it)
            }
        }
    }

    /**
     * Deletes an attendee's information from the database
     *
     * Consider calling this method when a user wants to logout from the app.
     * @param eventId ID of the event
     * @param token Token to identify attendee
     */
    suspend fun deleteAttendee(eventId: String, token: String) {
        dbHelper.deleteAttendee(eventId, token)
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
     * Fetches [LocalizedObject] for specified event using given id
     * @param eventId ID of the event
     * @param roomId ID of the room
     * @return null if schedule hasn't been cached yet; room otherwise
     */
    suspend fun getRoom(eventId: String, roomId: String): LocalizedObject? {
        return dbHelper.getRoom(eventId, roomId)
    }

    /**
     * Fetches [LocalizedObject] for specified event using given id
     * @param eventId ID of the event
     * @param tagId ID of the speaker
     * @return null if schedule hasn't been cached yet; tag otherwise
     */
    suspend fun getTag(eventId: String, tagId: String): LocalizedObject? {
        return dbHelper.getTag(eventId, tagId)
    }

    /**
     * Fetches [LocalizedObject] for specified event using given id
     * @param eventId ID of the event
     * @param sessionTypeId ID of the session type
     * @return null if schedule hasn't been cached yet; session's type otherwise
     */
    suspend fun getSessionType(eventId: String, sessionTypeId: String): LocalizedObject? {
        return dbHelper.getSessionType(eventId, sessionTypeId)
    }

    /**
     * Fetches [Session] for specified event using given id
     * @param eventId ID of the event
     * @param sessionId ID of the session
     * @return null if schedule hasn't been cached yet; session otherwise
     */
    suspend fun getSession(eventId: String, sessionId: String): Session? {
        return dbHelper.getSession(eventId, sessionId)
    }
}
