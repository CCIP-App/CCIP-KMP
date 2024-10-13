/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network.models.fastpass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Attendee(
    val attr: Map<String, String> = emptyMap(),

    @SerialName("event_id")
    val eventId: String,

    @SerialName("first_use")
    val firstUse: Long,

    val role: String,
    val scenarios: List<Scenario>,
    val token: String,

    @SerialName("user_id")
    val userId: String
)
