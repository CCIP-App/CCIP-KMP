/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.session

import android.text.format.DateUtils
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.components.TopAppBar
import app.opass.ccip.android.ui.extensions.browse
import app.opass.ccip.android.ui.navigation.Screen
import app.opass.ccip.network.models.schedule.Session
import com.mikepenz.markdown.m3.Markdown

@Composable
fun Screen.Session.SessionScreen(
    navHostController: NavHostController,
    viewModel: SessionViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val session by viewModel.session.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.getSession(this@SessionScreen.eventId, this@SessionScreen.sessionId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(navHostController = navHostController) }
    ) { paddingValues ->
        if (session != null) {
            LoadSession(
                session = session!!,
                dateTime = DateUtils.formatDateRange(
                    context,
                    viewModel.sdf.parse(session!!.start)!!.time,
                    viewModel.sdf.parse(session!!.end)!!.time,
                    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_ALL
                ),
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun LoadSession(session: Session, dateTime: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Tags
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            session.tags?.fastForEach { tag ->
                FilterChip(selected = true, onClick = {}, label = { Text(text = tag) })
            }
        }

        // Title
        Text(text = session.title, style = MaterialTheme.typography.headlineMedium)

        // SessionInfoItems
        SessionInfoItems(session.type, session.room, dateTime, session.speakers)

        // Description
        SessionDescItem(session.description)
    }
}

@Composable
private fun SessionInfoItems(sessionType: String?, room: String, dateTime: String, speakers: List<String>) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            sessionType?.let { type ->
                SessionInfoItem(
                    text = type.replaceFirstChar { it.titlecase() },
                    drawableRes = R.drawable.ic_info
                )
            }
            SessionInfoItem(
                text = room.ifBlank { stringResource(R.string.na) },
                drawableRes = R.drawable.ic_location
            )
            SessionInfoItem(text = dateTime, drawableRes = R.drawable.ic_time)
            SessionInfoItem(
                text = speakers.joinToString(separator = ", ")
                    .ifBlank { stringResource(R.string.na) },
                drawableRes = R.drawable.ic_podium
            )
        }
    }
}

@Composable
private fun SessionDescItem(description: String) {
    Card(
        modifier = Modifier.padding(vertical = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        val context = LocalContext.current
        val uriHandler = object : UriHandler {
            override fun openUri(uri: String) {
                context.browse(uri)
            }

        }
        CompositionLocalProvider(LocalUriHandler provides uriHandler) {
            Markdown(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                content = description
            )
        }
    }
}

@Composable
private fun SessionInfoItem(text: String, @DrawableRes drawableRes: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            painter = painterResource(drawableRes),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}
