/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.models

import androidx.annotation.StringRes

data class LanguageOptionItem(
    val languageTag: String,
    @StringRes val localNameResId: Int,
    @StringRes val translatedNameResId: Int
)
