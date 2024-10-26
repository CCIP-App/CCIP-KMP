/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.extensions

import app.opass.ccip.database.AnnouncementTable
import app.opass.ccip.network.models.fastpass.Announcement
import kotlinx.serialization.json.Json

internal fun AnnouncementTable.toAnnouncement(): Announcement {
    return Announcement(
        datetime = this.datetime,
        _msg_en = this.msg_en,
        _msg_zh = this.msg_zh,
        role = Json.decodeFromString<List<String>>(this.role),
        url = this.url
    )
}
