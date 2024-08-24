/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network.models.eventconfig

import kotlinx.serialization.Serializable

@Serializable
data class WiFi(
    val SSID: String,
    val password: String
)
