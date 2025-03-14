/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network.models.event

import app.opass.ccip.extensions.localized
import app.opass.ccip.network.models.common.LocalizedString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    @SerialName("display_name")
    val _name: LocalizedString,

    @SerialName("event_id")
    val id: String,

    @SerialName("logo_url")
    val logoUrl: String
) {
    val name: String
        get() = _name.localized()

    val isLogoTinted: Boolean
        get() = id in tintedLogos

    /**
     * List of event ID whose logos are either colorful or already tinted
     */
    private val tintedLogos = listOf(
        "devfest_taipei_2024",
        "HKOSCon_2024",
        "devfest_taipei_2023"
    )
}
