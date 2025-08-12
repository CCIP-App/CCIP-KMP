/*
 * SPDX-FileCopyrightText: 2024-2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.event

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.composable.FeatureComposable
import app.opass.ccip.android.ui.composable.TopAppBarComposable
import app.opass.ccip.android.extensions.browse
import app.opass.ccip.android.ui.menu.LanguageDropdownMenu
import app.opass.ccip.android.utils.WifiUtil
import app.opass.ccip.network.models.eventconfig.EventConfig
import app.opass.ccip.network.models.eventconfig.Feature
import app.opass.ccip.network.models.eventconfig.FeatureType
import app.opass.ccip.network.models.fastpass.Attendee
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun EventScreen(
    eventId: String,
    onNavigateToTicket: (token: String?) -> Unit,
    onNavigateToSchedule: () -> Unit,
    onNavigateToAnnouncement: (token: String?) -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: EventViewModel = hiltViewModel { factory: EventViewModel.Factory ->
        factory.create(eventId)
    }
) {
    val eventConfig by viewModel.eventConfig.collectAsStateWithLifecycle()
    val attendee by viewModel.attendee.collectAsStateWithLifecycle()

    ScreenContent(
        attendee = attendee,
        eventConfig = eventConfig,
        onNavigateUp = onNavigateUp,
        onNavigateToTicket = { onNavigateToTicket(attendee?.token) },
        onNavigateToSchedule = onNavigateToSchedule,
        onNavigateToAnnouncement = { onNavigateToAnnouncement(attendee?.token) }
    )
}

@Composable
private fun ScreenContent(
    attendee: Attendee? = null,
    eventConfig: EventConfig? = null,
    onNavigateToTicket: () -> Unit = {},
    onNavigateToSchedule: () -> Unit = {},
    onNavigateToAnnouncement: () -> Unit = {},
    onNavigateUp: () -> Unit = {},
) {
    val context = LocalContext.current
    val windowWidth = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

    var shouldShowLanguagePicker by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarComposable(
                title = eventConfig?.name ?: String(),
                subtitle = attendee?.userId ?: String(),
                navigationIcon = R.drawable.ic_drawer,
                onNavigate = onNavigateUp,
                actions = {
                    IconButton(onClick = { shouldShowLanguagePicker = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_languages),
                            contentDescription = stringResource(R.string.switch_language)
                        )
                    }

                    LanguageDropdownMenu(
                        expanded = shouldShowLanguagePicker,
                        onHideDropdownMenu = { shouldShowLanguagePicker = false }
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(eventConfig?.logoUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_landscape),
                error = painterResource(R.drawable.ic_broken_image),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(horizontal = 60.dp)
                    .aspectRatio(2.0f)
                    .heightIn(max = 180.dp)
                    .clip(RoundedCornerShape(10.dp)),
                colorFilter = if (eventConfig?.isLogoTinted != true) {
                    ColorFilter.tint(MaterialTheme.colorScheme.primary)
                } else {
                    null
                }
            )

            // Don't setup features if event config is not available
            if (eventConfig == null) return@Column

            FlowRow(
                modifier = Modifier.padding(10.dp),
                maxItemsInEachRow = if (windowWidth == WindowWidthSizeClass.COMPACT) 4 else 6,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                eventConfig.features.fastForEach { feature ->

                    // Return early if feature is limited to certain attendee roles
                    // Roles requires attendee to be logged in by verifying their ticket
                    if (!feature.roles.isNullOrEmpty() && !feature.roles!!.contains(attendee?.role)) {
                        return@fastForEach
                    }

                    EventFeature(
                        feature = feature,
                        onNavigateToTicket = onNavigateToTicket,
                        onNavigateToSchedule = onNavigateToSchedule,
                        onNavigateToAnnouncement = onNavigateToAnnouncement,
                        onBrowse = { url -> context.browse(url) },
                        onConnectToWiFi = {
                            WifiUtil.installOrSuggestNetworks(context, feature.wifi!!)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EventFeature(
    feature: Feature,
    onNavigateToTicket: () -> Unit,
    onNavigateToSchedule: () -> Unit,
    onNavigateToAnnouncement: () -> Unit,
    onBrowse: (url: String) -> Unit,
    onConnectToWiFi: () -> Unit
) {
    val context = LocalContext.current

    when (feature.type) {
        FeatureType.ANNOUNCEMENT -> {
            FeatureComposable(
                label = stringResource(id = R.string.announcement),
                icon = R.drawable.ic_announcement,
                onClicked = onNavigateToAnnouncement
            )
        }

        FeatureType.FAST_PASS -> {
            FeatureComposable(
                label = stringResource(id = R.string.fast_pass),
                icon = R.drawable.ic_logo
            )
        }

        FeatureType.IM -> {
            FeatureComposable(
                label = stringResource(id = R.string.irc),
                icon = R.drawable.ic_im,
                onClicked = { onBrowse(feature.url!!) }
            )
        }

        FeatureType.PUZZLE -> {
            FeatureComposable(
                label = stringResource(id = R.string.puzzle),
                icon = R.drawable.ic_puzzle,
                onClicked = { onBrowse(feature.url!!) }
            )
        }

        FeatureType.SCHEDULE -> {
            FeatureComposable(
                label = stringResource(id = R.string.schedule),
                icon = R.drawable.ic_schedule,
                onClicked = onNavigateToSchedule
            )
        }

        FeatureType.SPONSORS -> {
            FeatureComposable(
                label = stringResource(id = R.string.sponsors),
                icon = R.drawable.ic_sponsor,
                onClicked = { onBrowse(feature.url!!) }
            )
        }

        FeatureType.STAFFS -> {
            FeatureComposable(
                label = stringResource(id = R.string.staffs),
                icon = R.drawable.ic_staff,
                onClicked = { onBrowse(feature.url!!) }
            )
        }

        FeatureType.TELEGRAM -> {
            FeatureComposable(
                label = stringResource(id = R.string.telegram),
                icon = R.drawable.ic_telegram,
                onClicked = { onBrowse(feature.url!!) }
            )
        }

        FeatureType.TICKET -> {
            FeatureComposable(
                label = stringResource(id = R.string.ticket),
                icon = R.drawable.ic_ticket,
                onClicked = onNavigateToTicket
            )
        }

        FeatureType.VENUE -> {
            FeatureComposable(
                label = stringResource(id = R.string.venue),
                icon = R.drawable.ic_venue,
                onClicked = { onBrowse(feature.url!!) }
            )
        }

        FeatureType.WEBVIEW -> {
            FeatureComposable(
                label = feature.label,
                icon = feature.iconUrl,
                onClicked = { onBrowse(feature.url!!) }
            )
        }

        FeatureType.WIFI -> {
            if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI)) return

            FeatureComposable(
                label = stringResource(id = R.string.wifi),
                icon = R.drawable.ic_wifi,
                isEnabled = !feature.wifi.isNullOrEmpty(),
                onClicked = onConnectToWiFi
            )
        }

        FeatureType.UNAVAILABLE -> return
    }
}

@PreviewScreenSizes
@Composable
private fun EventScreenPreview() {
    ScreenContent()
}
