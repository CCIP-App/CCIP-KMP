/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.extensions

import app.opass.ccip.database.EventTable
import app.opass.ccip.network.models.common.LocalizedString
import app.opass.ccip.network.models.event.Event

internal fun EventTable.toEvent(): Event {
    return Event(
        _name = LocalizedString(en = this.nameEn, zh = this.nameZh),
        id = this.id,
        logoUrl = this.logoUrl
    )
}
