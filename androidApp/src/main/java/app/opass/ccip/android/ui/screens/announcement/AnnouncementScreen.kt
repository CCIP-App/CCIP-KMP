/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.announcement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.composable.AnnouncementComposable
import app.opass.ccip.android.ui.composable.TopAppBarComposable
import app.opass.ccip.android.ui.extensions.browse
import app.opass.ccip.network.models.fastpass.Announcement

@Composable
fun AnnouncementScreen(
    eventId: String,
    token: String? = null,
    onNavigateUp: () -> Unit = {},
    viewModel: AnnouncementViewModel = hiltViewModel()
) {
    val announcements by viewModel.announcements.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) { viewModel.getAnnouncements(eventId, token) }

    ScreenContent(
        announcements = announcements,
        onNavigateUp = onNavigateUp,
        onRefresh = { viewModel.getAnnouncements(eventId, token, true) }
    )
}

@Composable
private fun ScreenContent(
    announcements: List<Announcement> = emptyList(),
    onNavigateUp: () -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarComposable(
                title = stringResource(R.string.announcement),
                onNavigate = onNavigateUp,
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_refresh),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(announcements) { announcement: Announcement ->
                AnnouncementComposable(announcement.message, announcement.url.isNotBlank()) {
                    context.browse(announcement.url)
                }
            }
        }
    }
}

@Preview
@Composable
private fun AnnouncementScreenPreview() {
    ScreenContent()
}
