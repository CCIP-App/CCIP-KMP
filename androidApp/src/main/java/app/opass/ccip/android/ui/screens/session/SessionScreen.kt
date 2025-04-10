/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.session

import android.content.Intent
import android.provider.CalendarContract
import android.text.format.DateUtils
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.composable.SessionInfoComposable
import app.opass.ccip.android.ui.composable.TopAppBarComposable
import app.opass.ccip.android.ui.extensions.browse
import app.opass.ccip.android.ui.extensions.toast
import app.opass.ccip.android.ui.menu.SessionMenu
import app.opass.ccip.android.ui.menu.item.SessionMenuItem
import app.opass.ccip.network.models.schedule.Session

private const val TAG = "SessionScreen"

@Composable
fun SessionScreen(
    eventId: String,
    sessionId: String,
    onNavigateUp: () -> Unit,
    viewModel: SessionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val session by viewModel.session.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) { viewModel.getSession(eventId, sessionId) }

    if (session != null) {
        val startTime = viewModel.sdf.parse(session!!.start)!!.time
        val endTime = viewModel.sdf.parse(session!!.end)!!.time

        ScreenContent(
            title = session!!.title,
            description = session!!.description,
            dateTime = DateUtils.formatDateRange(
                context,
                startTime,
                endTime,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_ALL
            ),
            sessionType = session!!.type,
            room = session!!.room,
            tags = session!!.tags,
            speakers = session!!.speakers,
            onNavigateUp = onNavigateUp,
            onAddToCalendar = {
                try {
                    context.startActivity(getCalendarIntent(session!!, startTime, endTime))
                } catch (exception: Exception) {
                    Log.e(TAG, "Failed to add event to the calendar app", exception)
                    context.toast(R.string.add_to_calendar_failed)
                }
            },
            onShareSession = {
                try {
                    context.startActivity(
                        Intent.createChooser(getShareIntent(session!!.url!!), "")
                    )
                } catch (exception: Exception) {
                    Log.e(TAG, "Failed to share session", exception)
                    context.toast(R.string.share_failed)
                }
            }
        )
    }
}

@Composable
private fun ScreenContent(
    title: String,
    description: String,
    dateTime: String,
    sessionType: String? = null,
    room: String? = null,
    tags: List<String>? = emptyList(),
    speakers: List<String> = emptyList(),
    onNavigateUp: () -> Unit = {},
    onAddToCalendar: () -> Unit = {},
    onShareSession: () -> Unit = {}
) {
    val context = LocalContext.current
    val uriHandler = object : UriHandler {
        override fun openUri(uri: String) {
            context.browse(uri)
        }
    }

    @Composable
    fun SetupMenu() {
        SessionMenu { sessionMenuItem ->
            when (sessionMenuItem) {
                SessionMenuItem.SHARE -> onShareSession()
                SessionMenuItem.ADD_TO_CALENDAR -> onAddToCalendar()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarComposable(
                onNavigate = onNavigateUp,
                actions = { SetupMenu() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            CompositionLocalProvider(LocalUriHandler provides uriHandler) {
                SessionInfoComposable(
                    title = title,
                    description = description,
                    sessionType = sessionType,
                    dateTime = dateTime,
                    room = room,
                    tags = tags,
                    speakers = speakers
                )
            }
        }
    }
}

private fun getShareIntent(url: String): Intent {
    return Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, url)
    }
}

private fun getCalendarIntent(session: Session, startTime: Long, endTime: Long): Intent {
    return Intent(Intent.ACTION_INSERT).apply {
        setData(CalendarContract.Events.CONTENT_URI)
        putExtra(CalendarContract.Events.TITLE, session.title)
        putExtra(CalendarContract.Events.DESCRIPTION, session.description)
        putExtra(CalendarContract.Events.EVENT_LOCATION, session.room)
        putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)

        // Extras for which Android doesn't have variables
        putExtra("url", session.url)
    }
}

@Preview
@Composable
private fun SessionScreenPreview() {
    ScreenContent(
        title = LoremIpsum(6).values.first(),
        description = LoremIpsum(50).values.first(),
        dateTime = "Mar 8, 9:10 - 10:00 AM",
        room = "TR211",
        sessionType = "Keynote",
        tags = listOf("Software / DevOps", "Beginner Friendly"),
        speakers = listOf("Team OPass")
    )
}
