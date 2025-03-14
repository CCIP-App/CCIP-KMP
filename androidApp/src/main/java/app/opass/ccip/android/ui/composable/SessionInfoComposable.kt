/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import app.opass.ccip.android.R
import com.mikepenz.markdown.m3.Markdown

/**
 * Composable to display complete session details
 * @param title Title of the session
 * @param description Description of the session
 * @param dateTime Date and time of the session
 * @param sessionType Type of the session (for e.g. Keynote)
 * @param room Room where the session is being held, can be null in case of event like break
 * @param tags Tags associated with the event
 * @param speakers Speakers of the event
 * @see SessionComposable
 */
@Composable
fun SessionInfoComposable(
    title: String,
    description: String,
    dateTime: String,
    sessionType: String? = null,
    room: String? = null,
    tags: List<String>? = emptyList(),
    speakers: List<String> = emptyList(),
) {
    val containerColor = MaterialTheme.colorScheme.surfaceContainer

    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)

        if (tags != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                tags.fastForEach { tag ->
                    FilterChip(selected = true, onClick = {}, label = { Text(text = tag) })
                }
            }
        }

        Card(colors = CardDefaults.cardColors(containerColor = containerColor)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (sessionType != null) {
                    SessionInfo(
                        text = sessionType.replaceFirstChar { it.titlecase() },
                        drawable = R.drawable.ic_info
                    )
                }

                SessionInfo(
                    text = if (room.isNullOrBlank()) stringResource(R.string.na) else room,
                    drawable = R.drawable.ic_location
                )

                SessionInfo(text = dateTime, drawable = R.drawable.ic_time)

                SessionInfo(
                    text = speakers.joinToString(separator = ", ")
                        .ifBlank { stringResource(R.string.na) },
                    drawable = R.drawable.ic_podium
                )
            }
        }

        if (description.isNotBlank()) {
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
private fun SessionInfo(text: String, @DrawableRes drawable: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            painter = painterResource(drawable),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
private fun SessionInfoComposablePreview() {
    SessionInfoComposable(
        title = LoremIpsum(4).values.first(),
        description = LoremIpsum(30).values.first(),
        dateTime = "Mar 8, 9:10 - 10:00 AM",
        room = "TR211",
        sessionType = "Keynote",
        tags = listOf("Software / DevOps", "Beginner Friendly"),
        speakers = listOf("Team OPass")
    )
}
