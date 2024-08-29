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
internal fun LocalizedString.localized() = localized(this.en, this.zh)

/**
 * Returns String as per device's current locale, defaults to english
 */
internal fun localized(en: String, zh: String): String {
    return when (languageCode) {
        "zh" -> zh
        else -> en
    }
}
