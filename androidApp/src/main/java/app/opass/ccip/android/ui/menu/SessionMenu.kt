/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.menu

import androidx.compose.foundation.layout.Box
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
 * @param hasBookmark Whether the session has been bookmarked
 * @param hasAlarm Whether the session has an active alarm for notifying the user
 * @param onMenuItemClicked Callback when a menu item has been clicked
 * @see SessionMenuItem
 */
@Composable
fun SessionMenu(
    hasBookmark: Boolean = false,
    hasAlarm: Boolean = false,
    onMenuItemClicked: (sessionMenuItem: SessionMenuItem) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    fun onClick(sessionMenuItem: SessionMenuItem) {
        onMenuItemClicked(sessionMenuItem)
        expanded = false
    }

    IconButton(onClick = { onClick(SessionMenuItem.BOOKMARK) }) {
        Icon(
            painter = painterResource(
                if (hasBookmark) {
                    R.drawable.ic_bookmark_filled
                } else {
                    R.drawable.ic_bookmark
                }
            ),
            contentDescription = stringResource(R.string.session_bookmark)
        )
    }

    IconButton(onClick = { onClick(SessionMenuItem.SET_ALARM) }) {
        Icon(
            painter = painterResource(
                if (hasAlarm) {
                    R.drawable.ic_notifications_active_filled
                } else {
                    R.drawable.ic_notifications_active
                }
            ),
            contentDescription = stringResource(R.string.session_alarm)
        )
    }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                painter = painterResource(R.drawable.ic_more_vert),
                contentDescription = stringResource(R.string.menu)
            )
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
