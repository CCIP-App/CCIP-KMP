/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.database

import app.opass.ccip.network.models.common.LocalizedString
import app.opass.ccip.network.models.event.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

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
                    name = LocalizedString(en = it.nameEn, zh = it.nameZh),
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
                        nameEn = it.name.en,
                        nameZh = it.name.zh
                    )
                }
            }
        }
    }
}
