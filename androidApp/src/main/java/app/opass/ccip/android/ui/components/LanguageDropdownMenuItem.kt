/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.components

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import app.opass.ccip.android.R

@Composable
fun LanguageDropdownMenuItem(
    languageTag: String,
    @StringRes localNameResId: Int,
    @StringRes translatedNameResId: Int,
    onHideDropdownMenu: () -> Unit = {}
) {
    DropdownMenuItem(
        text = {
            Column(modifier = Modifier.padding(horizontal = 2.dp, vertical = 10.dp)) {
                Text(
                    text = stringResource(id = localNameResId),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(id = translatedNameResId),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        },
        onClick = {
            onHideDropdownMenu()

            AppCompatDelegate.setApplicationLocales(
                (
                    if (languageTag == "x-default") {
                        LocaleListCompat.getEmptyLocaleList()
                    } else {
                        LocaleListCompat.forLanguageTags(languageTag)
                    }
                )
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarPreview() {
    LanguageDropdownMenuItem(
        languageTag = "zh-Hant-TW",
        localNameResId = R.string.lang_local_name_zh_hant_tw,
        translatedNameResId = R.string.lang_translated_name_zh_hant_tw
    )
}
