/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.extensions

import app.opass.ccip.database.SessionTable
import app.opass.ccip.network.models.schedule.Session
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

internal fun SessionTable.toSession(): Session {
    return Session(
        _id = Json.encodeToJsonElement(this.id),
        _en = Session.Localized(description = this.descriptionEn, title = this.titleEn),
        _zh = Session.Localized(description = this.descriptionZh, title = this.titleZh),
        start = this.start,
        end = this.end,
        room = this.room,
        speakers = Json.decodeFromString<List<String>>(this.speakers),
        language = this.language,
        qa = this.qa,
        record = this.record,
        slide = this.slide,
        tags = this.tags?.let { t -> Json.decodeFromString<List<String>>(t) } ?: emptyList(),
        type = this.type,
        broadcast = this.broadcast?.let { b -> Json.decodeFromString<List<String>>(b) }
            ?: emptyList(),
        liveUrl = this.liveUrl,
        url = this.url,
        coWriteUrl = this.coWriteUrl
    )
}
