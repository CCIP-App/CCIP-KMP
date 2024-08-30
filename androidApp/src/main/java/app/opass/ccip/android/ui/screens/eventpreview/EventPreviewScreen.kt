/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.eventpreview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import app.opass.ccip.android.MainViewModel
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.components.TopAppBar
import app.opass.ccip.android.ui.extensions.sharedPreferences
import app.opass.ccip.android.ui.extensions.shimmer
import app.opass.ccip.android.ui.navigation.Screen
import app.opass.ccip.android.utils.Preferences.CURRENT_EVENT_ID
import app.opass.ccip.network.models.event.Event
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Screen.EventPreview.EventPreviewScreen(
    navHostController: NavHostController,
    viewModel: MainViewModel
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = stringResource(id = this.title)) }
    ) { paddingValues ->

        val sharedPreferences = LocalContext.current.sharedPreferences
        val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
        val events by viewModel.events.collectAsStateWithLifecycle()

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.getEvents(true) },
            modifier = Modifier.padding(paddingValues)
        ) {
            LazyColumn {
                if (events.isNullOrEmpty()) {
                    items(20) {
                        EventPreviewItem(
                            name = "                                   ",
                            logoUrl = String(),
                            isLoading = true
                        )
                    }
                } else {
                    items(items = events!!, key = { e -> e.id }) { event: Event ->
                        EventPreviewItem(name = event.name, logoUrl = event.logoUrl) {
                            sharedPreferences.edit { putString(CURRENT_EVENT_ID, event.id) }
                            navHostController.navigate(Screen.Event(event.id))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventPreviewItem(
    name: String,
    logoUrl: String,
    isLoading: Boolean = false,
    onClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clickable { onClicked() }
            .padding(20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(logoUrl)
                    .placeholder(R.drawable.ic_event)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .requiredSize(64.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmer(isLoading),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Text(
                text = name,
                fontSize = 20.sp,
                modifier = Modifier.shimmer(isLoading),
                maxLines = 1
            )
        }
        IconButton(onClick = onClicked, enabled = !isLoading) {
            Image(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            )
        }
    }
}
