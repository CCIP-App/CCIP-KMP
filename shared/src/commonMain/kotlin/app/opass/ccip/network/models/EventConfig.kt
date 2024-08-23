/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventConfig(
    @SerialName("display_name")
    val name: LocalizedString,

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
)
