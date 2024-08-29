/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network.models.schedule

import app.opass.ccip.extensions.localized
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val end: String,
    val id: String,
    val language: Language?,
    val qa: String?,
    val record: String?,
    val room: String,
    val slide: String?,
    val speakers: List<String>,
    val start: String,
    val tags: List<String>?,
    val type: String?,
    val broadcast: List<String>?,
    val live: String?,

    @SerialName("uri")
    val url: String?,

    @SerialName("co_write")
    val coWrite: String?,

    @SerialName("en")
    val _en: Localized,

    @SerialName("zh")
    val _zh: Localized
) {
    val title: String
        get() = localized(_en.title, _zh.title)

    val description: String
        get() = localized(_en.description, _zh.description)

    @Serializable
    data class Localized(
        val description: String,
        val title: String
    )
}
