/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.network.models.common

import app.opass.ccip.extensions.localized
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocalizedObject(
    @SerialName("en")
    val _en: En,

    val id: String,

    @SerialName("zh")
    val _zh: Zh
) {
    val name: String
        get() = localized(_en.name, _zh.name)
}

@Serializable
data class En(
    val name: String
)

@Serializable
data class Zh(
    val name: String
)
