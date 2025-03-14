/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.extensions.shimmer
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

/**
 * Composable to show event details
 * @param name Name of the event
 * @param logoUrl URL of the event logo
 * @param isLoading Whether to play shimmer animation on the composable to indicate loading progress
 * @param isLogoTinted Whether the logo is colored or already tinted
 * @param onClicked Callback when this composable is clicked
 */
@Composable
fun EventComposable(
    name: String,
    logoUrl: String,
    isLoading: Boolean = false,
    isLogoTinted: Boolean = false,
    onClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClicked() }
            .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(logoUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_event),
                error = painterResource(R.drawable.ic_broken_image),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .requiredSize(64.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmer(isLoading),
                colorFilter = if (!isLogoTinted) {
                    ColorFilter.tint(MaterialTheme.colorScheme.primary)
                } else {
                    null
                }
            )
            Text(
                text = name,
                fontSize = 18.sp,
                modifier = Modifier.shimmer(isLoading),
                maxLines = 1
            )
        }
        IconButton(onClick = onClicked, enabled = !isLoading) {
            Image(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventComposablePreview() {
    EventComposable(
        name = "SITCON 2025",
        logoUrl = "https://sitcon.org/branding/assets/logos/withname-white.png"
    )
}
