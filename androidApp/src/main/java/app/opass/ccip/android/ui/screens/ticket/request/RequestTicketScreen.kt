/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.ticket.request

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.composable.TopAppBarComposable
import app.opass.ccip.android.ui.dialog.ManualEntryDialog
import app.opass.ccip.android.ui.dialog.ProgressDialog
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun RequestTicketScreen(
    eventId: String,
    onNavigateUp: () -> Unit,
    onNavigateToShowTicket: (token: String) -> Unit,
    onNavigateToScanTicket: () -> Unit,
    viewModel: RequestTicketViewModel = hiltViewModel()
) {
    val token by viewModel.token.collectAsStateWithLifecycle()
    val eventConfig by viewModel.eventConfig.collectAsStateWithLifecycle()
    val isVerifying by viewModel.isVerifying.collectAsStateWithLifecycle()

    var shouldShowProgressDialog by rememberSaveable { mutableStateOf(false) }
    var shouldShowManualEntryDialog by rememberSaveable { mutableStateOf(false) }

    val startActivityForResult = rememberLauncherForActivityResult(
        contract = PickVisualMedia(),
        onResult = { uri ->
            // TODO: Process the image
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = RequestPermission(),
        onResult = { isGranted -> if (isGranted) onNavigateToScanTicket() }
    )

    LaunchedEffect(key1 = Unit) { viewModel.getEventConfig(eventId) }
    LaunchedEffect(key1 = isVerifying) { shouldShowProgressDialog = isVerifying }
    LaunchedEffect(key1 = token) { token?.let(onNavigateToShowTicket) }

    if (isVerifying) {
        ProgressDialog(onDismiss = { shouldShowProgressDialog = false })
    }

    if (shouldShowManualEntryDialog) {
        ManualEntryDialog(
            onConfirm = { manualToken ->
                shouldShowManualEntryDialog = false
                if (!manualToken.isNullOrBlank()) viewModel.getAttendee(eventId, manualToken)
            },
            onDismiss = { shouldShowManualEntryDialog = false }
        )
    }

    ScreenContent(
        eventLogoUrl = eventConfig?.logoUrl,
        onNavigateUp = onNavigateUp,
        onScanTicket = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
        onManualEntry = { shouldShowManualEntryDialog = true },
        onPickTicket = {
            startActivityForResult.launch(
                PickVisualMediaRequest(PickVisualMedia.ImageOnly)
            )
        }
    )
}

@Composable
private fun ScreenContent(
    eventLogoUrl: String? = null,
    onNavigateUp: () -> Unit = {},
    onManualEntry: () -> Unit = {},
    onScanTicket: () -> Unit = {},
    onPickTicket: () -> Unit = {}
) {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarComposable(
                title = stringResource(R.string.ticket),
                onNavigate = onNavigateUp,
                actions = {
                    IconButton(onClick = onManualEntry) {
                        Icon(
                            painter = painterResource(R.drawable.ic_keyboard),
                            contentDescription = stringResource(R.string.enter_token_manually_title)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(eventLogoUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_landscape),
                error = painterResource(R.drawable.ic_broken_image),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .aspectRatio(2.0f)
                    .heightIn(max = 180.dp)
                    .clip(RoundedCornerShape(10.dp)),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )

            HelpSection()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
            ) {
                if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                    Button(onClick = onScanTicket) {
                        Text(text = stringResource(R.string.scan_qr_code))
                    }
                }

                FilledTonalButton(onClick = onPickTicket) {
                    Icon(
                        painter = painterResource(R.drawable.ic_gallery),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text(text = stringResource(R.string.select_from_gallery))
                }
            }
        }
    }
}

@Composable
private fun HelpSection() {
    Card(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_qr_code),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = stringResource(R.string.ticket_verification_help_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.ticket_verification_help_desc),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun RequestTicketScreenPreview() {
    ScreenContent()
}
