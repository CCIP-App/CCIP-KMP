/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.extensions

import app.opass.ccip.database.RoomTable
import app.opass.ccip.database.SessionTypeTable
import app.opass.ccip.database.TagTable
import app.opass.ccip.network.models.common.En
import app.opass.ccip.network.models.common.LocalizedObject
import app.opass.ccip.network.models.common.Zh

internal fun Any.toLocalizedObject(): LocalizedObject? {
    return when (this) {
        is RoomTable -> getLocalizedObject(this.id, this.nameEn, this.nameZh)
        is TagTable -> getLocalizedObject(this.id, this.nameEn, this.nameZh)
        is SessionTypeTable -> getLocalizedObject(this.id, this.nameEn, this.nameZh)
        else -> null
    }
}

private fun getLocalizedObject(id: String, nameEn: String, nameZh: String): LocalizedObject {
    return LocalizedObject(id = id, _en = En(name = nameEn), _zh = Zh(name = nameZh))
}
