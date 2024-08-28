/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.extensions

import java.util.Locale

internal actual val languageCode: String
    get() = Locale.getDefault().language
