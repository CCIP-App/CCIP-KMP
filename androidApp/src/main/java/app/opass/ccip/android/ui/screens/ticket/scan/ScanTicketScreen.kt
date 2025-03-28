/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.ticket.scan

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.dialog.ProgressDialog
import app.opass.ccip.android.ui.extensions.toast

@Composable
fun ScanTicketScreen(
    eventId: String,
    onNavigateUp: () -> Unit,
    onNavigateToShowTicket: (token: String) -> Unit,
    viewModel: ScanTicketViewModel = hiltViewModel { factory: ScanTicketViewModel.Factory ->
        factory.create(eventId)
    }
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()
    val isVerifying by viewModel.isVerifying.collectAsStateWithLifecycle()

    var shouldShowProgressDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) { viewModel.bindToCamera(lifecycleOwner) }
    LaunchedEffect(key1 = isVerifying) { shouldShowProgressDialog = isVerifying }
    LaunchedEffect(key1 = Unit) {
        viewModel.token.collect { token ->
            if (token.isNullOrBlank()) {
                context.toast(R.string.ticket_verification_failed)
            } else {
                onNavigateToShowTicket(token)
            }
        }
    }

    if (isVerifying) {
        ProgressDialog(onDismiss = { shouldShowProgressDialog = false })
    }

    ScreenContent(
        surfaceRequest = surfaceRequest,
        onNavigateUp = onNavigateUp
    )
}

@Composable
private fun ScreenContent(surfaceRequest: SurfaceRequest? = null, onNavigateUp: () -> Unit = {}) {
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .clip(RoundedCornerShape(20.dp)),
        ) {
            surfaceRequest?.let { CameraXViewfinder(surfaceRequest = it) }
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ScanTicketScreenPreview() {
    ScreenContent()
}
