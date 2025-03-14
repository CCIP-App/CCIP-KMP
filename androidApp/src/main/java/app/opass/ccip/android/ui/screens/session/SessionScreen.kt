/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.session

import android.text.format.DateUtils
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
import app.opass.ccip.android.ui.composable.SessionInfoComposable
import app.opass.ccip.android.ui.composable.TopAppBarComposable
import app.opass.ccip.android.ui.extensions.browse

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
        ScreenContent(
            title = session!!.title,
            description = session!!.description,
            dateTime = DateUtils.formatDateRange(
                context,
                viewModel.sdf.parse(session!!.start)!!.time,
                viewModel.sdf.parse(session!!.end)!!.time,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_ALL
            ),
            sessionType = session!!.type,
            room = session!!.room,
            tags = session!!.tags,
            speakers = session!!.speakers,
            onNavigateUp = onNavigateUp
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
    onNavigateUp: () -> Unit = {}
) {
    val context = LocalContext.current
    val uriHandler = object : UriHandler {
        override fun openUri(uri: String) {
            context.browse(uri)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBarComposable(onNavigate = onNavigateUp) }
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
