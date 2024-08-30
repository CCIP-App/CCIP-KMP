/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.database

import app.opass.ccip.network.models.common.DateTime
import app.opass.ccip.network.models.common.LocalizedString
import app.opass.ccip.network.models.event.Event
import app.opass.ccip.network.models.eventconfig.EventConfig
import app.opass.ccip.network.models.eventconfig.Feature
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
}
