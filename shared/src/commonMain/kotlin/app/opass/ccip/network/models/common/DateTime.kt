/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network.models.common

import kotlinx.serialization.Serializable

@Serializable
data class DateTime(
    val end: String,
    val start: String
)
