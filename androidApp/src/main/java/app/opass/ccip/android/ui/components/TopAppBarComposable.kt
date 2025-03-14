/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.opass.ccip.android.R

/**
 * A reusable center-aligned top app bar
 * @param title Title of the screen
 * @param subtitle Subtitle of the screen
 * @param navigationIcon Navigation icon, fallbacks to [R.drawable.ic_arrow_back] if null
 * @param onNavigate Callback when the navigation icon is clicked
 * @param actions Menu actions to show on the top app bar
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBarComposable(
    title: String? = null,
    subtitle: String? = null,
    @DrawableRes navigationIcon: Int? = null,
    onNavigate: () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    CenterAlignedTopAppBar(
        title = {
            if (title != null) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge
                    )
                    if (!subtitle.isNullOrBlank()) {
                        Text(
                            text = subtitle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigate) {
                Icon(
                    painter = painterResource(navigationIcon ?: R.drawable.ic_arrow_back),
                    contentDescription = null
                )
            }
        },
        actions = actions
    )
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarPreview() {
    val context = LocalContext.current
    TopAppBarComposable(
        title = context.getString(R.string.select_event)
    )
}
