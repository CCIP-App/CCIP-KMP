/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.opass.ccip.android.R

/**
 * Composable to show language details in a dropdown menu
 * @param localNameResId Local name of the language
 * @param translatedNameResId Localized name of the language
 * @param isSelected Whether this language is currently selected
 * @param onClick Callback when this composable is clicked
 */
@Composable
fun LanguageDropdownMenuItem(
    @StringRes localNameResId: Int,
    @StringRes translatedNameResId: Int,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
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
        onClick = onClick,
        modifier = Modifier.background(
            color = if (isSelected) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MenuDefaults.containerColor
            }
        ),
        trailingIcon = {
            if (isSelected) {
                Icon(painter = painterResource(R.drawable.ic_check), contentDescription = null)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarSelectedPreview() {
    LanguageDropdownMenuItem(
        localNameResId = R.string.lang_translated_name_x_default,
        translatedNameResId = R.string.lang_translated_name_x_default,
        isSelected = true
    )
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarPreview() {
    LanguageDropdownMenuItem(
        localNameResId = R.string.lang_local_name_zh_hant_tw,
        translatedNameResId = R.string.lang_translated_name_zh_hant_tw
    )
}
