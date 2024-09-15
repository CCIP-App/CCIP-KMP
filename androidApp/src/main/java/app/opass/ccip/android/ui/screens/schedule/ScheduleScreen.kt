/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.schedule

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.components.TopAppBar
import app.opass.ccip.android.ui.extensions.shimmer
import app.opass.ccip.android.ui.navigation.Screen
import app.opass.ccip.android.ui.screens.event.EventViewModel
import app.opass.ccip.network.models.schedule.Session
import kotlinx.coroutines.launch

@Composable
fun Screen.Schedule.ScheduleScreen(
    navHostController: NavHostController,
    viewModel: EventViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val schedule by viewModel.schedule.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) { viewModel.getSchedule(this@ScheduleScreen.id) }

    @Composable
    fun LoadSessionPreviewItems(
        sessions: Map<String, List<Session>>?,
        modifier: Modifier = Modifier
    ) {
        if (sessions == null) {
            // TODO: Show shimmer while loading
            return
        }

        val tabData = sessions.keys.toList()
        val pagerState = rememberPagerState { tabData.size }
        val tabIndex = pagerState.currentPage
        val coroutineScope = rememberCoroutineScope()

        Column(modifier = modifier) {
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
                        key = { s -> s.id }) { session ->
                        SessionPreviewItem(
                            title = session.title,
                            startTime = DateUtils.formatDateTime(
                                context,
                                viewModel.sdf.parse(session.start)!!.time,
                                DateUtils.FORMAT_SHOW_TIME
                            ),
                            endTime = DateUtils.formatDateTime(
                                context,
                                viewModel.sdf.parse(session.end)!!.time,
                                DateUtils.FORMAT_SHOW_TIME
                            ),
                            room = session.room
                        )
                    }
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = stringResource(id = this.title), navHostController = navHostController)
        }
    ) { paddingValues ->
        LoadSessionPreviewItems(
            modifier = Modifier.padding(paddingValues),
            sessions = schedule?.sessions?.groupBy {
                DateUtils.formatDateTime(
                    context,
                    viewModel.sdf.parse(it.start)!!.time,
                    DateUtils.FORMAT_SHOW_DATE
                )
            }
        )
    }
}

@Composable
fun SessionPreviewItem(
    title: String,
    startTime: String,
    endTime: String,
    room: String,
    isLoading: Boolean = false,
    onClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClicked() }
            .padding(15.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = room.ifBlank { stringResource(R.string.na) },
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 12.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 10.dp)
                    .shimmer(isLoading),
            )
            Text(
                text = "$startTime ~ $endTime",
                fontSize = 12.sp,
                modifier = Modifier.shimmer(isLoading),
            )
        }
        Text(
            text = title,
            fontSize = 18.sp,
            modifier = Modifier.shimmer(isLoading),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
