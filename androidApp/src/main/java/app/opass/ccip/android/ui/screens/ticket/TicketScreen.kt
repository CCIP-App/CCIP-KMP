/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.ticket

import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.components.TopAppBar
import app.opass.ccip.android.ui.extensions.autoBrighten
import app.opass.ccip.android.ui.extensions.popBackToEventScreen
import app.opass.ccip.android.ui.extensions.sharedPreferences
import app.opass.ccip.android.ui.extensions.shimmer
import app.opass.ccip.android.ui.navigation.Screen
import app.opass.ccip.android.utils.CommonUtil.setBrightness
import app.opass.ccip.android.utils.ZXingUtil
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.coroutines.android.awaitFrame

@Composable
fun Screen.Ticket.TicketScreen(
    navHostController: NavHostController,
    viewModel: TicketViewModel = hiltViewModel()
) {

    // Always pop back to reflect latest status of attendee
    BackHandler {
        navHostController.popBackToEventScreen(this.eventId)
    }

    val context = LocalContext.current
    val token by viewModel.token.collectAsStateWithLifecycle()
    if (!token.isNullOrBlank()) {
        DisposableEffect(Unit) {
            if (context.sharedPreferences.autoBrighten) setBrightness(context, isFull = true)
            onDispose {
                setBrightness(context, isFull = false)
            }
        }
        ShowTicket(this, navHostController, viewModel)
    } else {
        RequestTicket(this, navHostController, viewModel)
    }
}

@Composable
private fun ShowTicket(
    screen: Screen.Ticket,
    navHostController: NavHostController,
    viewModel: TicketViewModel
) {
    val token by viewModel.token.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = stringResource(screen.title),
                navHostController = navHostController,
                actions = {
                    IconButton(onClick = { viewModel.logout(screen.eventId, token!!) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_logout),
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
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // QR
            Card(
                modifier = Modifier.padding(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                Image(
                    bitmap = ZXingUtil.generateQR(token!!).asImageBitmap(),
                    contentDescription = null
                )
            }

            // Brightness Control
            BrightnessControl()
        }
    }
}

@Composable
private fun RequestTicket(
    screen: Screen.Ticket,
    navHostController: NavHostController,
    viewModel: TicketViewModel
) {
    val context = LocalContext.current

    val eventConfig by viewModel.eventConfig.collectAsStateWithLifecycle()
    val isVerifying by viewModel.isVerifying.collectAsStateWithLifecycle()

    var shouldShowVerificationDialog by rememberSaveable { mutableStateOf(false) }
    var shouldShowManualEntryDialog by rememberSaveable { mutableStateOf(false) }

    val startActivityForResult = rememberLauncherForActivityResult(
        contract = PickVisualMedia(),
        onResult = { uri ->
            // TODO: Process the image
        }
    )

    LaunchedEffect(key1 = Unit) { viewModel.getEventConfig(screen.eventId) }
    LaunchedEffect(key1 = isVerifying) { shouldShowVerificationDialog = isVerifying }

    if (isVerifying) {
        VerificationDialog(onDismiss = { shouldShowVerificationDialog = false })
    }

    if (shouldShowManualEntryDialog) {
        ManualEntryDialog(
            onConfirm = {
                shouldShowManualEntryDialog = false
                viewModel.getAttendee(eventConfig!!.id, it)
            },
            onDismiss = { shouldShowManualEntryDialog = false }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = stringResource(screen.title),
                navHostController = navHostController,
                actions = {
                    IconButton(onClick = { shouldShowManualEntryDialog = true }) {
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
            HeaderImage(logoUrl = eventConfig?.logoUrl)

            // Tip
            HelpSection()

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
            ) {
                if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                    Button(onClick = {}) {
                        Text(text = stringResource(R.string.scan_qr_code))
                    }
                }
                FilledTonalButton(
                    onClick = {
                        startActivityForResult.launch(
                            PickVisualMediaRequest(PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
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
private fun HeaderImage(logoUrl: String?) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(logoUrl)
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
            .clip(RoundedCornerShape(10.dp))
            .shimmer(logoUrl == null),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
    )
}

@Composable
private fun BrightnessControl() {
    val context = LocalContext.current
    var isOverridingBrightness by rememberSaveable {
        mutableStateOf(context.sharedPreferences.autoBrighten)
    }

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_brightness_high),
                contentDescription = null
            )
            Text(text = stringResource(R.string.auto_brighten))
        }
        Switch(
            checked = isOverridingBrightness,
            onCheckedChange = {
                isOverridingBrightness = it
                context.sharedPreferences.autoBrighten(it)
                setBrightness(context, it)
            }
        )
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

@Composable
private fun VerificationDialog(onDismiss: () -> Unit = {}) {
    Dialog(onDismissRequest = { onDismiss() }) {
        CircularProgressIndicator(modifier = Modifier.requiredWidth(48.dp))
    }
}

@Composable
private fun ManualEntryDialog(onConfirm: (token: String) -> Unit = {}, onDismiss: () -> Unit = {}) {
    var token by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(focusRequester) {
        awaitFrame()
        focusRequester.requestFocus()
    }

    AlertDialog(
        title = { Text(text = stringResource(R.string.enter_token_manually_title)) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = stringResource(R.string.enter_token_manually_desc))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = token,
                    onValueChange = { token = it },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }
        },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton (
                onClick = { onConfirm(token) },
                enabled = token.isNotBlank()
            ) {
                Text(text = stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = stringResource(android.R.string.cancel))
            }
        }
    )
}
