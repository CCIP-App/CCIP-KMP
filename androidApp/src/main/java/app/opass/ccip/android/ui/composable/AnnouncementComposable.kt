/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composable to show announcement details
 * @param message Announcement message
 * @param isClickable Whether the announcement composable is clickable
 * @param onClicked Callback when the composable is clicked
 */
@Composable
fun AnnouncementComposable(
    message: String,
    dateTime: String,
    isClickable: Boolean,
    onClicked: () -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .clickable(enabled = isClickable) { onClicked() },
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(text = message, style = MaterialTheme.typography.bodyLarge)
            Text(text = dateTime, fontSize = 12.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AnnouncementComposablePreview() {
    AnnouncementComposable(
        message = LoremIpsum(11).values.first(),
        dateTime = "Aug 3, 2024, 9:00 AM",
        isClickable = false
    )
}
