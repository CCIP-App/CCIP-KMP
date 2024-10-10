/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.database

import app.opass.ccip.extensions.toEvent
import app.opass.ccip.extensions.toEventConfig
import app.opass.ccip.extensions.toLocalizedObject
import app.opass.ccip.extensions.toSession
import app.opass.ccip.extensions.toSpeaker
import app.opass.ccip.network.models.common.LocalizedObject
import app.opass.ccip.network.models.event.Event
import app.opass.ccip.network.models.eventconfig.EventConfig
import app.opass.ccip.network.models.schedule.Schedule
import app.opass.ccip.network.models.schedule.Session
import app.opass.ccip.network.models.schedule.Speaker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

internal class OPassDatabaseHelper {

    companion object {
        const val FILE_NAME = "opass.db"
    }

    private val database = OPassDatabase(DriverFactory.createDriver())
    private val dbQuery = database.oPassDatabaseQueries

    suspend fun getAllEvents(): List<Event> {
        return withContext(Dispatchers.IO) {
            dbQuery.selectAllEvents().executeAsList().map { it.toEvent() }
        }
    }

    suspend fun addEvents(events: List<Event>) {
        withContext(Dispatchers.IO) {
            dbQuery.transaction {
                dbQuery.deleteAllEvents()
                events.forEach {
                    dbQuery.insertEvent(
                        id = it.id,
                        logoUrl = it.logoUrl,
                        nameEn = it._name.en,
                        nameZh = it._name.zh
                    )
                }
            }
        }
    }

    suspend fun getEventConfig(eventId: String): EventConfig? {
        return withContext(Dispatchers.IO) {
            dbQuery.getEventConfig(eventId).executeAsOneOrNull()?.toEventConfig()
                ?: return@withContext null
        }
    }

    suspend fun addEventConfig(eventConfig: EventConfig) {
        return withContext(Dispatchers.IO) {
            dbQuery.transaction {
                dbQuery.deleteEventConfig(eventConfig.id)
                dbQuery.insertEventConfig(
                    id = eventConfig.id,
                    logoUrl = eventConfig.logoUrl,
                    nameEn = eventConfig._name.en,
                    nameZh = eventConfig._name.zh,
                    eventStart = eventConfig.dateTime.start,
                    eventEnd = eventConfig.dateTime.end,
                    website = eventConfig.website,
                    features = Json.encodeToString(eventConfig.features),
                    publishStart = eventConfig.publish.start,
                    publishEnd = eventConfig.publish.end
                )
            }
        }
    }

    suspend fun getRoom(eventId: String, roomId: String): LocalizedObject? {
        return withContext(Dispatchers.IO) {
            dbQuery.selectRoom(eventId, roomId).executeAsOneOrNull()
                ?.toLocalizedObject()
        }
    }

    private suspend fun getRooms(eventId: String): List<LocalizedObject> {
        return withContext(Dispatchers.IO) {
            dbQuery.selectAllRooms(eventId).executeAsList().map { it.toLocalizedObject()!! }
        }
    }

    suspend fun addRooms(eventId: String, rooms: List<LocalizedObject>) {
        withContext(Dispatchers.IO) {
            dbQuery.transaction {
                dbQuery.deleteAllRooms(eventId = eventId)
                rooms.forEach {
                    dbQuery.insertRoom(
                        id = it.id,
                        nameEn = it._en.name,
                        nameZh = it._zh.name,
                        eventId = eventId
                    )
                }
            }
        }
    }

    suspend fun getTag(eventId: String, tagId: String): LocalizedObject? {
        return withContext(Dispatchers.IO) {
            dbQuery.selectTag(eventId, tagId).executeAsOneOrNull()
                ?.toLocalizedObject()
        }
    }

    private suspend fun getTags(eventId: String): List<LocalizedObject> {
        return withContext(Dispatchers.IO) {
            dbQuery.selectAllTags(eventId).executeAsList().map { it.toLocalizedObject()!! }
        }
    }

    suspend fun addTags(eventId: String, tags: List<LocalizedObject>) {
        withContext(Dispatchers.IO) {
            dbQuery.transaction {
                dbQuery.deleteAllTags(eventId = eventId)
                tags.forEach {
                    dbQuery.insertTag(
                        id = it.id,
                        nameEn = it._en.name,
                        nameZh = it._zh.name,
                        eventId = eventId
                    )
                }
            }
        }
    }

    suspend fun getSessionType(eventId: String, sessionTypeId: String): LocalizedObject? {
        return withContext(Dispatchers.IO) {
            dbQuery.selectSessionType(eventId, sessionTypeId).executeAsOneOrNull()
                ?.toLocalizedObject()
        }
    }

    private suspend fun getSessionTypes(eventId: String): List<LocalizedObject> {
        return withContext(Dispatchers.IO) {
            dbQuery.selectAllSessionTypes(eventId).executeAsList().map { it.toLocalizedObject()!! }
        }
    }

    suspend fun addSessionTypes(eventId: String, sessionTypes: List<LocalizedObject>) {
        withContext(Dispatchers.IO) {
            dbQuery.transaction {
                dbQuery.deleteAllSessionTypes(eventId = eventId)
                sessionTypes.forEach {
                    dbQuery.insertSessionType(
                        id = it.id,
                        nameEn = it._en.name,
                        nameZh = it._zh.name,
                        eventId = eventId
                    )
                }
            }
        }
    }

    suspend fun getSpeaker(eventId: String, speakerId: String): Speaker? {
        return withContext(Dispatchers.IO) {
            dbQuery.selectSpeaker(eventId, speakerId).executeAsOneOrNull()?.toSpeaker()
        }
    }

    private suspend fun getSpeakers(eventId: String): List<Speaker> {
        return withContext(Dispatchers.IO) {
            dbQuery.selectAllSpeakers(eventId).executeAsList().map { it.toSpeaker() }
        }
    }

    suspend fun addSpeakers(eventId: String, speakers: List<Speaker>) {
        withContext(Dispatchers.IO) {
            dbQuery.transaction {
                dbQuery.deleteAllSpeakers(eventId)
                speakers.forEach {
                    dbQuery.insertSpeaker(
                        id = it.id,
                        avatarUrl = it.avatarUrl,
                        nameEn = it._en.name,
                        nameZh = it._zh.name,
                        bioEn = it._en.bio,
                        bioZh = it._zh.bio,
                        eventId = eventId
                    )
                }
            }
        }
    }

    suspend fun getSession(eventId: String, sessionId: String): Session? {
        return withContext(Dispatchers.IO) {
            dbQuery.selectSession(
                eventId, Json.decodeFromString(sessionId)
            ).executeAsOneOrNull()?.toSession()
        }
    }

    private suspend fun getSessions(eventId: String): List<Session> {
        return withContext(Dispatchers.IO) {
            dbQuery.selectAllSessions(eventId).executeAsList().map { it.toSession() }
        }
    }

    suspend fun addSessions(eventId: String, sessions: List<Session>) {
        withContext(Dispatchers.IO) {
            dbQuery.transaction {
                dbQuery.deleteAllSessions(eventId)
                sessions.forEach {
                    dbQuery.insertSession(
                        id = it.id,
                        titleEn = it._en.title,
                        titleZh = it._zh.title,
                        descriptionEn = it._en.description,
                        descriptionZh = it._zh.description,
                        start = it.start,
                        end = it.end,
                        room = it.room,
                        speakers = Json.encodeToString(it.speakers),
                        language = it.language,
                        qa = it.qa,
                        record = it.record,
                        slide = it.slide,
                        tags = Json.encodeToString(it.tags ?: emptyList()),
                        type = it.type,
                        broadcast = Json.encodeToString(it.broadcast ?: emptyList()),
                        liveUrl = it.liveUrl,
                        url = it.url,
                        coWriteUrl = it.coWriteUrl,
                        eventId = eventId
                    )
                }
            }
        }
    }

    suspend fun getSchedule(eventId: String): Schedule {
        return Schedule(
            rooms = getRooms(eventId),
            tags = getTags(eventId),
            sessionTypes = getSessionTypes(eventId),
            speakers = getSpeakers(eventId),
            sessions = getSessions(eventId)
        )
    }
}
