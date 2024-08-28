/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.extensions

import app.opass.ccip.network.models.common.LocalizedString

internal expect val languageCode: String

/**
 * Returns String as per device's current locale, defaults to english
 */
internal fun LocalizedString.localized(): String {
    return when (languageCode) {
        "zh" -> this.zh
        else -> this.en
    }
}
