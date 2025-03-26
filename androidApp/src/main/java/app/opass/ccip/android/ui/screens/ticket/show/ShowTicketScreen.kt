/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.ticket.show

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.composable.TipComposable
import app.opass.ccip.android.ui.composable.TopAppBarComposable
import app.opass.ccip.android.ui.dialog.LogoutDialog
import app.opass.ccip.android.ui.extensions.autoBrighten
import app.opass.ccip.android.ui.extensions.overrideBrightness
import app.opass.ccip.android.ui.extensions.sharedPreferences
import app.opass.ccip.android.utils.ZXingUtil

@Composable
fun ShowTicketScreen(
    eventId: String,
    token: String,
    onNavigateUp: () -> Unit,
    onNavigateToRequestTicket: () -> Unit,
    viewModel: ShowTicketViewModel = hiltViewModel()
) {
    val activity = LocalActivity.current
    val context = LocalContext.current
    val view = LocalView.current
    var isOverridingBrightness by rememberSaveable {
        mutableStateOf(context.sharedPreferences.autoBrighten)
    }

    var shouldShowLogoutDialog by rememberSaveable { mutableStateOf(false) }

    DisposableEffect(Unit) {
        activity?.overrideBrightness(context.sharedPreferences.autoBrighten)
        view.keepScreenOn = true
        onDispose {
            activity?.overrideBrightness(false)
            view.keepScreenOn = false
        }
    }

    if (shouldShowLogoutDialog) {
        LogoutDialog(
            onConfirm = {
                shouldShowLogoutDialog = false
                viewModel.logout(eventId, token)
                onNavigateToRequestTicket()
            },
            onDismiss = { shouldShowLogoutDialog = false }
        )
    }

    ScreenContent(
        token = token,
        onNavigateUp = onNavigateUp,
        isOverridingBrightness = isOverridingBrightness,
        onLogout = { shouldShowLogoutDialog = true },
        onBrightnessOverridden = { isChecked ->
            isOverridingBrightness = isChecked
            context.sharedPreferences.autoBrighten(isChecked)
            activity!!.overrideBrightness(isChecked)
        }
    )
}

@Composable
fun ScreenContent(
    token: String,
    isOverridingBrightness: Boolean = true,
    onNavigateUp: () -> Unit = {},
    onLogout: () -> Unit = {},
    onBrightnessOverridden: (isOverridingBrightness: Boolean) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarComposable(
                title = stringResource(R.string.ticket),
                onNavigate = onNavigateUp,
                actions = {
                    IconButton(onClick = onLogout) {
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
            Card(
                modifier = Modifier.padding(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                Image(
                    bitmap = ZXingUtil.generateQR(token).asImageBitmap(),
                    contentDescription = null
                )
            }

            TipComposable(
                title = R.string.ticket_privacy_help_title,
                description = R.string.ticket_privacy_help_desc,
                icon = R.drawable.ic_qr_code
            )

            BrightnessControlSwitch(
                isOverridingBrightness = isOverridingBrightness,
                onSwitched = onBrightnessOverridden
            )
        }
    }
}

@Composable
fun BrightnessControlSwitch(
    isOverridingBrightness: Boolean = true,
    onSwitched: (isChecked: Boolean) -> Unit = {}
) {
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
            onCheckedChange = onSwitched
        )
    }
}

@Preview
@Composable
private fun ShowTicketScreenPreview() {
    // Taken from https://github.com/CCIP-App/CCIP-Server/blob/master/app/reg-sample.csv
    ScreenContent(token = "7679f08f7eaeef5e9a65a1738ae2840e")
}
