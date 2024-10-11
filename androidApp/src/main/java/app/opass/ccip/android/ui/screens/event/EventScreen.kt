/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.event

import android.content.pm.PackageManager
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.window.core.layout.WindowWidthSizeClass
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.components.TopAppBar
import app.opass.ccip.android.ui.extensions.browse
import app.opass.ccip.android.ui.extensions.shimmer
import app.opass.ccip.android.ui.navigation.Screen
import app.opass.ccip.android.ui.screens.eventpreview.EventPreviewScreen
import app.opass.ccip.android.utils.WifiUtil
import app.opass.ccip.network.models.eventconfig.FeatureType
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
fun Screen.Event.EventScreen(
    navHostController: NavHostController,
    viewModel: EventViewModel = hiltViewModel()
) {

    val windowWidth = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val context = LocalContext.current
    val eventConfig by viewModel.eventConfig.collectAsStateWithLifecycle()

    var shouldShowBottomSheet by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) { viewModel.getEventConfig(this@EventScreen.id) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = eventConfig?.name ?: String(),
                navigationIcon = {
                    IconButton(onClick = { shouldShowBottomSheet = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_drawer),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        if (shouldShowBottomSheet) {
            ModalBottomSheet(onDismissRequest = { shouldShowBottomSheet = false }) {
                Screen.EventPreview.EventPreviewScreen(
                    navHostController = navHostController,
                    isPullToRefreshEnabled = false,
                    containerColor = BottomSheetDefaults.ContainerColor
                ) {
                    shouldShowBottomSheet = false
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderImage(logoUrl = eventConfig?.logoUrl)
            if (eventConfig != null) {
                FlowRow(
                    modifier = Modifier.padding(10.dp),
                    maxItemsInEachRow = if (windowWidth == WindowWidthSizeClass.COMPACT) 4 else 6,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // TODO: Show and hide features based on roles
                    eventConfig!!.features.fastForEach { feature ->
                        when (feature.type) {
                            FeatureType.ANNOUNCEMENT -> {
                                FeatureItem(
                                    label = stringResource(id = R.string.announcement),
                                    iconRes = R.drawable.ic_announcement
                                )
                            }

                            FeatureType.FAST_PASS -> {
                                FeatureItem(
                                    label = stringResource(id = R.string.fast_pass),
                                    iconRes = R.drawable.ic_logo
                                )
                            }

                            FeatureType.IM -> {
                                FeatureItem(
                                    label = stringResource(id = R.string.irc),
                                    iconRes = R.drawable.ic_im
                                ) {
                                    context.browse(feature.url!!)
                                }
                            }

                            FeatureType.PUZZLE -> {
                                FeatureItem(
                                    label = stringResource(id = R.string.puzzle),
                                    iconRes = R.drawable.ic_puzzle
                                ) {
                                    context.browse(feature.url!!)
                                }
                            }

                            FeatureType.SCHEDULE -> {
                                FeatureItem(
                                    label = stringResource(id = R.string.schedule),
                                    iconRes = R.drawable.ic_schedule
                                ) {
                                    navHostController.navigate(Screen.Schedule(this@EventScreen.id))
                                }
                            }

                            FeatureType.SPONSORS -> {
                                FeatureItem(
                                    label = stringResource(id = R.string.sponsors),
                                    iconRes = R.drawable.ic_sponsor
                                ) {
                                    context.browse(feature.url!!)
                                }
                            }

                            FeatureType.STAFFS -> {
                                FeatureItem(
                                    label = stringResource(id = R.string.staff),
                                    iconRes = R.drawable.ic_staff
                                ) {
                                    context.browse(feature.url!!)
                                }
                            }

                            FeatureType.TELEGRAM -> {
                                FeatureItem(
                                    label = stringResource(id = R.string.telegram),
                                    iconRes = R.drawable.ic_telegram
                                ) {
                                    context.browse(feature.url!!)
                                }
                            }

                            FeatureType.TICKET -> {
                                FeatureItem(
                                    label = stringResource(id = R.string.ticket),
                                    iconRes = R.drawable.ic_ticket
                                )
                            }

                            FeatureType.VENUE -> {
                                FeatureItem(
                                    label = stringResource(id = R.string.venue),
                                    iconRes = R.drawable.ic_venue
                                ) {
                                    context.browse(feature.url!!)
                                }
                            }

                            FeatureType.WEBVIEW -> {
                                FeatureItem(label = feature.label, iconUrl = feature.iconUrl) {
                                    context.browse(feature.url!!)
                                }
                            }

                            FeatureType.WIFI -> {
                                if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI)) {
                                    FeatureItem(
                                        label = stringResource(id = R.string.wifi),
                                        iconRes = R.drawable.ic_wifi,
                                        isEnabled = !feature.wifi.isNullOrEmpty()
                                    ) {
                                        WifiUtil.installOrSuggestNetworks(context, feature.wifi!!)
                                    }
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderImage(logoUrl: String?) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(logoUrl)
            .placeholder(R.drawable.ic_landscape)
            .error(R.drawable.ic_broken_image)
            .crossfade(true)
            .build(),
        contentDescription = "",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .aspectRatio(2.0f)
            .heightIn(max = 180.dp)
            .shimmer(logoUrl == null),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
    )
}

@Composable
private fun FeatureItem(
    label: String,
    @DrawableRes iconRes: Int? = null,
    iconUrl: String? = null,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    onClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .width(75.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(enabled = isEnabled) { onClicked() }
            .padding(vertical = 5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .requiredSize(64.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(iconRes ?: iconUrl)
                    .placeholder(R.drawable.ic_event)
                    .error(R.drawable.ic_broken_image)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .requiredSize(32.dp)
                    .fillMaxSize()
                    .shimmer(isLoading),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        }
        Text(
            text = label,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
