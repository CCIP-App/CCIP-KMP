/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network.models.fastpass

import app.opass.ccip.extensions.localized
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Announcement(
    val datetime: Long,

    @SerialName("msg_en")
    val _msg_en: String,

    @SerialName("msg_zh")
    val _msg_zh: String,

    val role: List<String>,

    @SerialName("uri")
    val url: String
) {
    val message: String
        get() = localized(_msg_en, _msg_zh)
}
