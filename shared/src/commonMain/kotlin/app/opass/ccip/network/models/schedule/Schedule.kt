/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network.models.schedule

import app.opass.ccip.network.models.common.LocalizedObject
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    val rooms: List<LocalizedObject>,
    val sessions: List<Session>,
    val speakers: List<Speaker>,
    val tags: List<LocalizedObject>,

    @SerialName("session_types")
    val sessionTypes: List<LocalizedObject>
)
