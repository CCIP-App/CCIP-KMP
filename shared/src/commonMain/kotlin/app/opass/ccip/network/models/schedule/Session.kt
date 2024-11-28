/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network.models.schedule

import app.opass.ccip.extensions.localized
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Session(
    val end: String,

    @SerialName("id")
    val _id: JsonElement,

    val language: String? = null,
    val qa: String? = null,
    val record: String? = null,
    val room: String,
    val slide: String? = null,
    val speakers: List<String>,
    val start: String,
    val tags: List<String>? = emptyList(),
    val type: String? = null,
    val broadcast: List<String>? = emptyList(),

    @SerialName("live")
    val liveUrl: String? = null,

    @SerialName("uri")
    val url: String? = null,

    @SerialName("co_write")
    val coWriteUrl: String? = null,

    @SerialName("en")
    val _en: Localized,

    @SerialName("zh")
    val _zh: Localized
) {
    val id: String
        get() = _id.toString()

    val title: String
        get() = localized(_en.title, _zh.title)

    val description: String
        get() = localized(_en.description, _zh.description)

    @Serializable
    data class Localized(
        val description: String = "",
        val title: String
    )
}
