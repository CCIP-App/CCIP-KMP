/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network.models.eventconfig

import app.opass.ccip.extensions.localized
import app.opass.ccip.network.models.common.LocalizedString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Feature(

    @SerialName("display_text")
    val _label: LocalizedString,

    @SerialName("feature")
    val type: FeatureType,

    @SerialName("icon")
    val iconUrl: String? = null,

    val url: String? = null,

    val wifi: List<WiFi>? = null,

    @SerialName("visible_roles")
    val roles: List<String>? = null
) {
    val label: String
        get() = _label.localized()
}
