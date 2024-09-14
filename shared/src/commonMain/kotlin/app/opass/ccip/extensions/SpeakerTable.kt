/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.extensions

import app.opass.ccip.database.SpeakerTable
import app.opass.ccip.network.models.schedule.Speaker

internal fun SpeakerTable.toSpeaker(): Speaker {
    return Speaker(
        id = this.id,
        avatarUrl = this.avatarUrl,
        _en = Speaker.Localized(bio = this.bioEn, name = this.nameEn),
        _zh = Speaker.Localized(bio = this.bioZh, name = this.nameZh)
    )
}
