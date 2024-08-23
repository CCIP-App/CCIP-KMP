/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class FeatureType {

    @SerialName("fastpass")
    FAST_PASS,

    @SerialName("schedule")
    SCHEDULE,

    @SerialName("announcement")
    ANNOUNCEMENT,

    @SerialName("puzzle")
    PUZZLE,

    @SerialName("ticket")
    TICKET,

    @SerialName("telegram")
    TELEGRAM,

    @SerialName("im")
    IM,

    @SerialName("sponsors")
    SPONSORS,

    @SerialName("staffs")
    STAFFS,

    @SerialName("venue")
    VENUE,

    @SerialName("webview")
    WEBVIEW,

    @SerialName("wifi")
    WIFI
}
