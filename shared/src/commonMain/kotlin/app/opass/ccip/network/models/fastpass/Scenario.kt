/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network.models.fastpass

import app.opass.ccip.extensions.localized
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Scenario(
    val attr: Map<String, String> = emptyMap(),

    @SerialName("available_time")
    val availableTime: Long,

    val countdown: Int,

    @SerialName("display_text")
    val _displayText: Localized,

    @SerialName("expire_time")
    val expireTime: Long,

    val disabled: String? = null,

    val id: String,
    val order: Int,
    val used: Int? = null
) {
    val displayText: String
        get() = localized(_displayText.en, _displayText.zh)

    @Serializable
    data class Localized(
        @SerialName("en-US")
        val en: String,

        @SerialName("zh-TW")
        val zh: String
    )
}
