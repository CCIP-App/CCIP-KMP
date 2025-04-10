/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.announcement

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    viewModel: AnnouncementViewModel = hiltViewModel { factory: AnnouncementViewModel.Factory ->
        factory.create(eventId, token)
    }
) {
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val announcements by viewModel.announcements.collectAsStateWithLifecycle()

    ScreenContent(
        announcements = announcements ?: emptyList(),
        isRefreshing = isRefreshing,
        onNavigateUp = onNavigateUp,
        onRefresh = { viewModel.getAnnouncements(true) }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ScreenContent(
    announcements: List<Announcement> = emptyList(),
    isRefreshing: Boolean = false,
    onNavigateUp: () -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarComposable(
                title = stringResource(R.string.announcement),
                onNavigate = onNavigateUp
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(announcements) { announcement: Announcement ->
                    AnnouncementComposable(
                        message = announcement.message,
                        isClickable = announcement.url.isNotBlank(),
                        dateTime = DateUtils.formatDateTime(
                            context,
                            announcement.dateTimeInMills,
                            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_ALL
                        )
                    ) {
                        context.browse(announcement.url)
                    }
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
