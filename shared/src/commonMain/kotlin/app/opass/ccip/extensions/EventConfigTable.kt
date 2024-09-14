/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.extensions

import app.opass.ccip.database.EventConfigTable
import app.opass.ccip.network.models.common.DateTime
import app.opass.ccip.network.models.common.LocalizedString
import app.opass.ccip.network.models.eventconfig.EventConfig
import app.opass.ccip.network.models.eventconfig.Feature
import kotlinx.serialization.json.Json

internal fun EventConfigTable.toEventConfig(): EventConfig {
    return EventConfig(
        _name = LocalizedString(en = this.nameEn, this.nameZh),
        id = this.id,
        logoUrl = this.logoUrl,
        dateTime = DateTime(end = this.eventEnd, start = this.eventStart),
        website = this.website,
        features = Json.decodeFromString<List<Feature>>(this.features),
        publish = DateTime(end = this.publishEnd, start = this.publishStart)
    )
}
