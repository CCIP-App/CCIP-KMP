/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.composable.EventComposable
import app.opass.ccip.android.ui.composable.SearchAppBarComposable
import app.opass.ccip.android.extensions.saveCurrentEventId
import app.opass.ccip.android.extensions.sharedPreferences
import app.opass.ccip.network.models.event.Event

@Composable
fun PreviewScreen(
    onNavigateUp: (() -> Unit)? = null,
    onNavigateToEvent: (eventId: String) -> Unit = {},
    viewModel: PreviewViewModel = hiltViewModel()
) {
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()

    ScreenContent(
        events = events ?: emptyList(),
        searchResult = searchResult,
        isRefreshing = isRefreshing,
        onNavigateUp = onNavigateUp,
        onNavigateToEvent = onNavigateToEvent,
        onSearch = { query -> viewModel.search(query) },
        onRefresh = { viewModel.getEvents(true) }
    )
}

@Composable
private fun ScreenContent(
    events: List<Event> = emptyList(),
    searchResult: List<Event> = emptyList(),
    isRefreshing: Boolean = false,
    onSearch: (query: String) -> Unit = {},
    onRefresh: () -> Unit = {},
    onNavigateUp: (() -> Unit)? = null,
    onNavigateToEvent: (eventId: String) -> Unit = {},
) {
    val sharedPreferences = LocalContext.current.sharedPreferences
    val state = rememberPullToRefreshState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchAppBarComposable(
                searchHint = R.string.search_event,
                isEnabled = events.isNotEmpty(),
                onSearch = onSearch,
                onNavigateUp = onNavigateUp
            ) {
                LazyColumn {
                    items(items = searchResult, key = { e -> e.id }) { event: Event ->
                        EventComposable(name = event.name, logoUrl = event.logoUrl) {
                            sharedPreferences.saveCurrentEventId(event.id)
                            onNavigateToEvent(event.id)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.padding(paddingValues),
            state = state,
            indicator = {
                PullToRefreshDefaults.LoadingIndicator(
                    state = state,
                    isRefreshing = isRefreshing,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        ) {
            LazyColumn(contentPadding = PaddingValues(horizontal = 20.dp)) {
                if (events.isEmpty()) {
                    items(20) {
                        EventComposable(
                            name = "                                   ",
                            logoUrl = String(),
                            isLoading = true
                        )
                    }
                } else {
                    items(items = events, key = { e -> e.id }) { event: Event ->
                        EventComposable(
                            name = event.name,
                            logoUrl = event.logoUrl,
                            isLogoTinted = event.isLogoTinted
                        ) {
                            sharedPreferences.saveCurrentEventId(event.id)
                            onNavigateToEvent(event.id)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewScreenPreview() {
    ScreenContent()
}
