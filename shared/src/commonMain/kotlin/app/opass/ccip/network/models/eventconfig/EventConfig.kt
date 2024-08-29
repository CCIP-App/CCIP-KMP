/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network.models.eventconfig

import app.opass.ccip.extensions.localized
import app.opass.ccip.network.models.common.DateTime
import app.opass.ccip.network.models.common.LocalizedString
import app.opass.ccip.network.models.schedule.Schedule
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventConfig(
    @SerialName("display_name")
    val _name: LocalizedString,

    @SerialName("event_id")
    val id: String,

    @SerialName("logo_url")
    val logoUrl: String,

    @SerialName("event_date")
    val dateTime: DateTime,

    @SerialName("event_website")
    val website: String?,

    val features: List<Feature>,

    val publish: DateTime
) {
    val name: String
        get() = _name.localized()
}
