/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEach
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.models.LanguageOptionItem

@Composable
fun LanguageDropdownMenu(
    expanded: Boolean,
    onHideDropdownMenu: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    val languageOptions = listOf(
        LanguageOptionItem(
            locale = "en-US",
            localNameResId = R.string.lang_local_name_en_us,
            translatedNameResId = R.string.lang_translated_name_en_us
        ),
        LanguageOptionItem(
            locale = "nan-Hant-TW",
            localNameResId = R.string.lang_local_name_nan_hant_tw,
            translatedNameResId = R.string.lang_translated_name_nan_hant_tw
        ),
        LanguageOptionItem(
            locale = "nan-Latn-TW-pehoeji",
            localNameResId = R.string.lang_local_name_nan_latn_tw_pehoeji,
            translatedNameResId = R.string.lang_translated_name_nan_latn_tw_pehoeji
        ),
        LanguageOptionItem(
            locale = "nan-Latn-TW-tailo",
            localNameResId = R.string.lang_local_name_nan_latn_tw_tailo,
            translatedNameResId = R.string.lang_translated_name_nan_latn_tw_tailo
        ),
        LanguageOptionItem(
            locale = "hi-IN",
            localNameResId = R.string.lang_local_name_hi_in,
            translatedNameResId = R.string.lang_translated_name_hi_in
        ),
        LanguageOptionItem(
            locale = "nb-NO",
            localNameResId = R.string.lang_local_name_nb_no,
            translatedNameResId = R.string.lang_translated_name_nb_no
        ),
        LanguageOptionItem(
            locale = "ta-IN",
            localNameResId = R.string.lang_local_name_ta_in,
            translatedNameResId = R.string.lang_translated_name_ta_in
        ),
        LanguageOptionItem(
            locale = "zh-Hant-TW",
            localNameResId = R.string.lang_local_name_zh_hant_tw,
            translatedNameResId = R.string.lang_translated_name_zh_hant_tw
        )
    )

    DropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest) {
        languageOptions.fastForEach { option ->
            LanguageDropdownMenuItem(
                key = option.locale,
                localNameResId = option.localNameResId,
                translatedNameResId = option.translatedNameResId,
                onHideDropdownMenu = onHideDropdownMenu
            )
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
