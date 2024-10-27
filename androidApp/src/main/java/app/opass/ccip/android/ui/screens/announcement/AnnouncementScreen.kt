/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.announcement

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.components.TopAppBar
import app.opass.ccip.android.ui.extensions.browse
import app.opass.ccip.android.ui.navigation.Screen
import app.opass.ccip.network.models.fastpass.Announcement

@Composable
fun Screen.Announcement.AnnouncementScreen(
    navHostController: NavHostController,
    viewModel: AnnouncementViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val announcements by viewModel.announcements.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.getAnnouncements(this@AnnouncementScreen.eventId, this@AnnouncementScreen.token)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = stringResource(this.title),
                navHostController = navHostController,
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.getAnnouncements(
                                this@AnnouncementScreen.eventId,
                                this@AnnouncementScreen.token,
                                true
                            )
                        }
                    ) {
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
                AnnouncementItem(announcement) {
                    context.browse(announcement.url)
                }
            }
        }
    }
}

@Composable
fun AnnouncementItem(announcement: Announcement, onClicked: () -> Unit = {}) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .clickable(enabled = announcement.url.isNotBlank()) { onClicked() }
        ) {
            Text(text = announcement.message, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
