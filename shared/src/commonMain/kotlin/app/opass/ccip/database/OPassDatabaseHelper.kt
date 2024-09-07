/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.database

import app.opass.ccip.network.models.common.DateTime
import app.opass.ccip.network.models.common.En
import app.opass.ccip.network.models.common.LocalizedObject
import app.opass.ccip.network.models.common.LocalizedString
import app.opass.ccip.network.models.common.Zh
import app.opass.ccip.network.models.event.Event
import app.opass.ccip.network.models.eventconfig.EventConfig
import app.opass.ccip.network.models.eventconfig.Feature
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
            dbQuery.selectAllEvents().executeAsList().map {
                Event(
                    _name = LocalizedString(en = it.nameEn, zh = it.nameZh),
                    id = it.id,
                    logoUrl = it.logoUrl
                )
            }
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
            val rawConfig = dbQuery.getEventConfig(eventId).executeAsOneOrNull() ?: return@withContext null
            EventConfig(
                _name = LocalizedString(en = rawConfig.nameEn, rawConfig.nameZh),
                id = rawConfig.id,
                logoUrl = rawConfig.logoUrl,
                dateTime = DateTime(end = rawConfig.eventEnd, start = rawConfig.eventStart),
                website = rawConfig.website,
                features = Json.decodeFromString<List<Feature>>(rawConfig.features),
                publish = DateTime(end = rawConfig.publishEnd, start = rawConfig.publishStart)
            )
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

    suspend fun getRooms(eventId: String): List<LocalizedObject> {
        return withContext(Dispatchers.IO) {
            dbQuery.selectAllRooms(eventId).executeAsList().map {
                LocalizedObject(
                    id = it.id,
                    _en = En(name = it.nameEn),
                    _zh = Zh(name = it.nameZh)
                )
            }
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

    suspend fun getTags(eventId: String): List<LocalizedObject> {
        return withContext(Dispatchers.IO) {
            dbQuery.selectAllTags(eventId).executeAsList().map {
                LocalizedObject(
                    id = it.id,
                    _en = En(name = it.nameEn),
                    _zh = Zh(name = it.nameZh)
                )
            }
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

    suspend fun getSessionTypes(eventId: String): List<LocalizedObject> {
        return withContext(Dispatchers.IO) {
            dbQuery.selectAllSessionTypes(eventId).executeAsList().map {
                LocalizedObject(
                    id = it.id,
                    _en = En(name = it.nameEn),
                    _zh = Zh(name = it.nameZh)
                )
            }
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

    suspend fun getSpeakers(eventId: String): List<Speaker> {
        return withContext(Dispatchers.IO) {
            dbQuery.selectAllSpeakers(eventId).executeAsList().map {
                Speaker(
                    id = it.id,
                    avatarUrl = it.avatarUrl,
                    _en = Speaker.Localized(bio = it.bioEn, name = it.nameEn),
                    _zh = Speaker.Localized(bio = it.bioZh, name = it.nameZh)
                )
            }
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

    suspend fun getSessions(eventId: String): List<Session> {
        return withContext(Dispatchers.IO) {
            dbQuery.selectAllSessions(eventId).executeAsList().map {
                Session(
                    id = it.id,
                    _en = Session.Localized(description = it.descriptionEn, title = it.titleEn),
                    _zh = Session.Localized(description = it.descriptionZh, title = it.titleZh),
                    start = it.start,
                    end = it.end,
                    room = it.room,
                    speakers = Json.decodeFromString<List<String>>(it.speakers),
                    language = it.language,
                    qa = it.qa,
                    record = it.record,
                    slide = it.slide,
                    tags = it.tags?.let { t -> Json.decodeFromString<List<String>>(t) } ?: emptyList(),
                    type = it.type,
                    broadcast = it.broadcast?.let { b -> Json.decodeFromString<List<String>>(b) } ?: emptyList(),
                    liveUrl = it.liveUrl,
                    url = it.url,
                    coWriteUrl = it.coWriteUrl
                )
            }
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
}
