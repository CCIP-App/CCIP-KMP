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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import app.opass.ccip.android.ui.components.TopAppBar
import app.opass.ccip.android.ui.extensions.shimmer
import app.opass.ccip.android.ui.navigation.Screen
import app.opass.ccip.android.ui.screens.event.EventViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun Screen.Schedule.ScheduleScreen(
    navHostController: NavHostController,
    viewModel: EventViewModel = hiltViewModel()
) {
    val schedule by viewModel.schedule.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) { viewModel.getSchedule(this@ScheduleScreen.id) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = stringResource(id = this.title), navHostController = navHostController)
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            if (schedule != null && schedule!!.sessions.isNotEmpty()) {
                items(items = schedule!!.sessions, key = { s -> s.id  }) { session ->
                    SessionPreviewItem(
                        title = session.title,
                        startDateTime = session.start,
                        endDateTime = session.end,
                        room = session.room
                    )
                }
            }
        }
    }
}

@Composable
fun SessionPreviewItem(
    title: String,
    startDateTime: String,
    endDateTime: String,
    room: String,
    isLoading: Boolean = false,
    onClicked: () -> Unit = {}
) {
    val context = LocalContext.current
    val startTime = DateUtils.formatDateTime(context, getSDFTime(startDateTime), DateUtils.FORMAT_SHOW_TIME)
    val endTime = DateUtils.formatDateTime(context, getSDFTime(endDateTime), DateUtils.FORMAT_SHOW_TIME)

    Column(
        modifier = Modifier
            .clickable { onClicked() }
            .padding(20.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = room,
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

private fun getSDFTime(dateTime: String): Long {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault())
    return sdf.parse(dateTime)!!.time
}
