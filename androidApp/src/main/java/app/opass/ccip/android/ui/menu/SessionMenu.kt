/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.menu.item.SessionMenuItem

/**
 * Menu for the session screen
 * @param onMenuItemClicked Callback when a menu item has been clicked
 * @see SessionMenuItem
 */
@Composable
fun SessionMenu(onMenuItemClicked: (sessionMenuItem: SessionMenuItem) -> Unit = {}) {
    var expanded by remember { mutableStateOf(false) }
    fun onClick(sessionMenuItem: SessionMenuItem) {
        onMenuItemClicked(sessionMenuItem)
        expanded = false
    }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.menu))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.add_to_calendar)) },
                onClick = { onClick(SessionMenuItem.ADD_TO_CALENDAR) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_calendar_add),
                        contentDescription = null
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.share)) },
                onClick = { onClick(SessionMenuItem.SHARE) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_share),
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SessionMenuPreview() {
    SessionMenu()
}
