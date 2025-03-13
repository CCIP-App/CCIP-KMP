/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.components.SearchAppBar
import app.opass.ccip.android.ui.extensions.popBackToEventScreen
import app.opass.ccip.android.ui.extensions.saveCurrentEventId
import app.opass.ccip.android.ui.extensions.sharedPreferences
import app.opass.ccip.android.ui.extensions.shimmer
import app.opass.ccip.android.ui.navigation.Screen
import app.opass.ccip.network.models.event.Event
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PreviewScreen(
    navHostController: NavHostController,
    viewModel: PreviewViewModel = hiltViewModel(),
    onEventSelected: () -> Unit = {}
) {
    val sharedPreferences = LocalContext.current.sharedPreferences
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchAppBar(
                searchHint = R.string.search_event,
                isEnabled = !events.isNullOrEmpty(),
                onSearch = { query -> viewModel.search(query) }
            ) {
                LazyColumn {
                    items(items = searchResult, key = { e -> e.id }) { event: Event ->
                        PreviewItem(name = event.name, logoUrl = event.logoUrl) {
                            sharedPreferences.saveCurrentEventId(event.id)
                            navHostController.navigate(Screen.Event(event.id))
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.getEvents(true) },
            modifier = Modifier.padding(paddingValues)
        ) {
            LazyColumn(contentPadding = PaddingValues(horizontal = 20.dp)) {
                if (events.isNullOrEmpty()) {
                    items(20) {
                        PreviewItem(
                            name = "                                   ",
                            logoUrl = String(),
                            isLoading = true
                        )
                    }
                } else {
                    items(items = events!!, key = { e -> e.id }) { event: Event ->
                        PreviewItem(name = event.name, logoUrl = event.logoUrl) {
                            onEventSelected()
                            sharedPreferences.saveCurrentEventId(event.id)
                            navHostController.popBackToEventScreen(event.id)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PreviewItem(
    name: String,
    logoUrl: String,
    isLoading: Boolean = false,
    onClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClicked() }
            .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(logoUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_event),
                error = painterResource(R.drawable.ic_broken_image),
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
                fontSize = 18.sp,
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
