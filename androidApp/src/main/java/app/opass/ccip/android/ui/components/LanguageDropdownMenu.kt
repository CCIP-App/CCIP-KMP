/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.models.LanguageOptionItem

@Composable
fun LanguageDropdownMenu(
    expanded: Boolean,
    onHideDropdownMenu: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    val languageOptions: Map<String, LanguageOptionItem> = mapOf(
        "en-US" to LanguageOptionItem(
            localNameResId = R.string.lang_local_name_en_us,
            translatedNameResId = R.string.lang_translated_name_en_us
        ),
        "nan-Hant-TW" to LanguageOptionItem(
            localNameResId = R.string.lang_local_name_nan_hant_tw,
            translatedNameResId = R.string.lang_translated_name_nan_hant_tw
        ),
        "nan-Latn-TW-pehoeji" to LanguageOptionItem(
            localNameResId = R.string.lang_local_name_nan_latn_tw_pehoeji,
            translatedNameResId = R.string.lang_translated_name_nan_latn_tw_pehoeji
        ),
        "nan-Latn-TW-tailo" to LanguageOptionItem(
            localNameResId = R.string.lang_local_name_nan_latn_tw_tailo,
            translatedNameResId = R.string.lang_translated_name_nan_latn_tw_tailo
        ),
        "hi-IN" to LanguageOptionItem(
            localNameResId = R.string.lang_local_name_hi_in,
            translatedNameResId = R.string.lang_translated_name_hi_in
        ),
        "nb-NO" to LanguageOptionItem(
            localNameResId = R.string.lang_local_name_nb_no,
            translatedNameResId = R.string.lang_translated_name_nb_no
        ),
        "ta-IN" to LanguageOptionItem(
            localNameResId = R.string.lang_local_name_ta_in,
            translatedNameResId = R.string.lang_translated_name_ta_in
        ),
        "zh-Hant-TW" to LanguageOptionItem(
            localNameResId = R.string.lang_local_name_zh_hant_tw,
            translatedNameResId = R.string.lang_translated_name_zh_hant_tw
        )
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        languageOptions.forEach { option ->
            LanguageDropdownMenuItem(
                key = option.key,
                localNameResId = option.value.localNameResId,
                translatedNameResId = option.value.translatedNameResId,
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
