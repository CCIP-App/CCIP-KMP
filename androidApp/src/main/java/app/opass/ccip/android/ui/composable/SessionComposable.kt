/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.extensions.shimmer

/**
 * Composable to show session details in a schedule
 * @param title Title of the session
 * @param startTime Starting time of the session
 * @param endTime Ending time of the session
 * @param room Room where the session is being held, can be null for events such as lunch or breaks
 * @param isLoading Whether to play shimmer animation on the composable to indicate loading progress
 * @param onClicked Callback when the composable is clicked
 */
@Composable
fun SessionComposable(
    title: String,
    startTime: String,
    endTime: String,
    room: String? = null,
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = room ?: stringResource(R.string.na),
                fontSize = 12.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
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

@Preview(showBackground = true)
@Composable
private fun SessionComposablePreviewSession() {
    SessionComposable(
        title = "Wikidata 下的地方資料收集：活用整個維基媒體與開放資料平台幫你建構地方資料集",
        startTime = "11:15",
        endTime = "11:45",
        room = "TR211"
    )
}

@Preview(showBackground = true)
@Composable
private fun SessionComposablePreviewBreak() {
    SessionComposable(
        title = "Coffee Break",
        startTime = "16:00",
        endTime = "16:30"
    )
}
