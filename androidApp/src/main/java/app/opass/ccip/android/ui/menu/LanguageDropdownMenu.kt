/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.menu

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEach
import androidx.core.os.LocaleListCompat
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.composable.LanguageDropdownMenuItem
import app.opass.ccip.android.ui.models.LanguageOptionItem

/**
 * Supported listed of app languages.
 *
 * This includes languages not present in Android/AOSP. Look into Weblate for possible localizations.
 */
private val languageOptions = listOf(
    LanguageOptionItem(
        languageTag = "x-default",
        localNameResId = R.string.lang_translated_name_x_default,
        translatedNameResId = R.string.lang_translated_name_x_default
    ),
    LanguageOptionItem(
        languageTag = "en-US",
        localNameResId = R.string.lang_local_name_en_us,
        translatedNameResId = R.string.lang_translated_name_en_us
    ),
    LanguageOptionItem(
        languageTag = "hi-IN",
        localNameResId = R.string.lang_local_name_hi_in,
        translatedNameResId = R.string.lang_translated_name_hi_in
    ),
    LanguageOptionItem(
        languageTag = "nan-Hant-TW",
        localNameResId = R.string.lang_local_name_nan_hant_tw,
        translatedNameResId = R.string.lang_translated_name_nan_hant_tw
    ),
    LanguageOptionItem(
        languageTag = "nan-Latn-TW-pehoeji",
        localNameResId = R.string.lang_local_name_nan_latn_tw_pehoeji,
        translatedNameResId = R.string.lang_translated_name_nan_latn_tw_pehoeji
    ),
    LanguageOptionItem(
        languageTag = "nan-Latn-TW-tailo",
        localNameResId = R.string.lang_local_name_nan_latn_tw_tailo,
        translatedNameResId = R.string.lang_translated_name_nan_latn_tw_tailo
    ),
    LanguageOptionItem(
        languageTag = "nb-NO",
        localNameResId = R.string.lang_local_name_nb_no,
        translatedNameResId = R.string.lang_translated_name_nb_no
    ),
    LanguageOptionItem(
        languageTag = "ta-IN",
        localNameResId = R.string.lang_local_name_ta_in,
        translatedNameResId = R.string.lang_translated_name_ta_in
    ),
    LanguageOptionItem(
        languageTag = "zh-Hans-CN",
        localNameResId = R.string.lang_local_name_zh_hans_cn,
        translatedNameResId = R.string.lang_translated_name_zh_hans_cn
    ),
    LanguageOptionItem(
        languageTag = "zh-Hant-TW",
        localNameResId = R.string.lang_local_name_zh_hant_tw,
        translatedNameResId = R.string.lang_translated_name_zh_hant_tw
    )
)

/**
 * Dropdown menu for changing app language.
 * @param expanded Whether the menu is expanded or not
 * @param onHideDropdownMenu Callback when the menu is hidden
 */
@Composable
fun LanguageDropdownMenu(expanded: Boolean, onHideDropdownMenu: () -> Unit = {}) {
    DropdownMenu(expanded = expanded, onDismissRequest = onHideDropdownMenu) {
        languageOptions.fastForEach { option ->
            LanguageDropdownMenuItem(
                localNameResId = option.localNameResId,
                translatedNameResId = option.translatedNameResId,
                isSelected = (
                    if (option.languageTag == "x-default") {
                        AppCompatDelegate.getApplicationLocales() == LocaleListCompat.getEmptyLocaleList()
                    } else {
                        AppCompatDelegate.getApplicationLocales() == LocaleListCompat.forLanguageTags(option.languageTag)
                    }
                )
            ) {
                AppCompatDelegate.setApplicationLocales(
                    if (option.languageTag == "x-default") {
                        LocaleListCompat.getEmptyLocaleList()
                    } else {
                        LocaleListCompat.forLanguageTags(option.languageTag)
                    }
                )
                onHideDropdownMenu()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarPreview() {
    LanguageDropdownMenu(
        expanded = true
    )
}
