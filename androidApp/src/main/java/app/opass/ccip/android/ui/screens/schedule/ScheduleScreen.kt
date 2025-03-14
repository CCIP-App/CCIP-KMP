/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.schedule

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEachIndexed
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.components.SessionComposable
import app.opass.ccip.android.ui.components.TopAppBar
import app.opass.ccip.android.ui.screens.event.EventViewModel
import app.opass.ccip.network.models.schedule.Session
import kotlinx.coroutines.launch

@Composable
fun ScheduleScreen(
    eventId: String,
    onNavigateUp: () -> Unit,
    onNavigateToSession: (sessionId: String) -> Unit,
    viewModel: EventViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val schedule by viewModel.schedule.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) { viewModel.getSchedule(eventId) }

    ScreenContent(
        sessions = schedule?.sessions?.groupBy {
            DateUtils.formatDateTime(
                context,
                viewModel.sdf.parse(it.start)!!.time,
                DateUtils.FORMAT_SHOW_DATE
            )
        } ?: emptyMap(),
        onNavigateUp = onNavigateUp,
        onNavigateToSession = onNavigateToSession,
        onFormatDateTime = { time ->
            DateUtils.formatDateTime(
                context,
                viewModel.sdf.parse(time)!!.time,
                DateUtils.FORMAT_SHOW_TIME
            )
        }
    )
}

@Composable
private fun ScreenContent(
    sessions: Map<String, List<Session>> = emptyMap(),
    onNavigateUp: () -> Unit = {},
    onNavigateToSession: (sessionId: String) -> Unit = {},
    onFormatDateTime: (time: String) -> String = { "" }
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.schedule),
                onNavigate = onNavigateUp
            )
        }
    ) { paddingValues ->
        val tabData = sessions.keys.toList()
        val pagerState = rememberPagerState { tabData.size }
        val tabIndex = pagerState.currentPage
        val coroutineScope = rememberCoroutineScope()

        Column(modifier = Modifier.padding(paddingValues)) {
            if (tabData.size > 1) {
                TabRow(selectedTabIndex = tabIndex) {
                    tabData.fastForEachIndexed { index, date ->
                        Tab(
                            selected = tabIndex == index,
                            text = { Text(text = date) },
                            onClick = {
                                coroutineScope.launch { pagerState.animateScrollToPage(index) }
                            }
                        )
                    }
                }
            }

            HorizontalPager(state = pagerState) { index ->
                LazyColumn {
                    items(
                        items = sessions.getValue(tabData[index]),
                        key = { s -> s.id }
                    ) { session ->
                        SessionComposable(
                            title = session.title,
                            startTime = onFormatDateTime(session.start),
                            endTime = onFormatDateTime(session.end),
                            room = session.room,
                            onClicked = { onNavigateToSession(session.id) }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ScheduleScreenPreview() {
    ScreenContent(
        sessions = mapOf(
            "August 3, 2024" to emptyList(),
            "August 4, 2024" to emptyList()
        ),
        onFormatDateTime = { time -> time }
    )
}
