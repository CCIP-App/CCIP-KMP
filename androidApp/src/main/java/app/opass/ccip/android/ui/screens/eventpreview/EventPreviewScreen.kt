/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.eventpreview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import app.opass.ccip.android.R
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
    viewModel: EventPreviewViewModel = hiltViewModel(),
    isPullToRefreshEnabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.background,
    onEventSelected: () -> Unit = {}
) {
    var query by rememberSaveable { mutableStateOf("") }
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    val sharedPreferences = LocalContext.current.sharedPreferences
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsStateWithLifecycle()

    @Composable
    fun LoadEventPreviewItems(list: List<Event>?, modifier: Modifier = Modifier) {
        LazyColumn(modifier = modifier) {
            if (events.isNullOrEmpty()) {
                items(20) {
                    EventPreviewItem(
                        name = "                                   ",
                        logoUrl = String(),
                        isLoading = true
                    )
                }
            } else {
                items(items = list!!, key = { e -> e.id }) { event: Event ->
                    EventPreviewItem(name = event.name, logoUrl = event.logoUrl) {
                        onEventSelected()
                        sharedPreferences.edit { putString(CURRENT_EVENT_ID, event.id) }
                        navHostController.navigate(Screen.Event(event.id)) {
                            popUpTo(navHostController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun EventSearchBar() {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = { query = it },
                        onSearch = { query = it },
                        expanded = isExpanded,
                        onExpandedChange = { isExpanded = it; query = String() },
                        enabled = !events.isNullOrEmpty(),
                        leadingIcon = {
                            if (isExpanded) {
                                IconButton(onClick = { isExpanded = false; query = String() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            } else {
                                Icon(Icons.Default.Search, contentDescription = null)
                            }
                        },
                        trailingIcon = {
                            if (isExpanded) {
                                IconButton(onClick = { query = String() }) {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = null
                                    )
                                }
                            } else {
                                IconButton(onClick = { viewModel.getEvents(true) }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_refresh),
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        placeholder = { Text(text = stringResource(id = R.string.search_event)) }
                    )
                },
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it; query = String() }
            ) {
                LazyColumn {
                    items(
                        items = events!!.filter { it.name.contains(query, true) },
                        key = { e -> e.id }
                    ) { event: Event ->
                        EventPreviewItem(name = event.name, logoUrl = event.logoUrl) {
                            sharedPreferences.edit { putString(CURRENT_EVENT_ID, event.id) }
                            navHostController.navigate(Screen.Event(event.id))
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { EventSearchBar() },
        containerColor = containerColor
    ) { paddingValues ->
        if (isPullToRefreshEnabled) {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { viewModel.getEvents(true) },
                modifier = Modifier.padding(paddingValues)
            ) {
                LoadEventPreviewItems(list = events)
            }
        } else {
            LoadEventPreviewItems(list = events, modifier = Modifier.padding(paddingValues))
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
                    .error(R.drawable.ic_broken_image)
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
