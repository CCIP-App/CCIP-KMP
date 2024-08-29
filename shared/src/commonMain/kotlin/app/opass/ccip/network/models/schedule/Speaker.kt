/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network.models.schedule

import app.opass.ccip.extensions.localized
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Speaker(
    val avatar: String,
    val id: String,

    @SerialName("en")
    val _en: Localized,

    @SerialName("zh")
    val _zh: Localized
) {
    val name: String
        get() = localized(_en.name, _zh.name)

    val bio: String
        get() = localized(_en.bio, _zh.bio)

    @Serializable
    data class Localized(
        val bio: String,
        val name: String
    )
}
