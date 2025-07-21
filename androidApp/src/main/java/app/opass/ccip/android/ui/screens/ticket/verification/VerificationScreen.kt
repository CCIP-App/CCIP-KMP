/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.screens.ticket.verification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.opass.ccip.android.R
import app.opass.ccip.android.ui.theme.OPassTheme

@Composable
fun VerificationScreen(
    eventId: String,
    token: String,
    onVerificationSuccess: (verifiedToken: String) -> Unit,
    onVerificationFailed: () -> Unit,
    viewModel: VerificationViewModel = hiltViewModel { factory: VerificationViewModel.Factory ->
        factory.create(eventId, token)
    }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = stringResource(R.string.verifying_token))
                }
                uiState.isVerified == true -> {
                    Text(
                        text = stringResource(R.string.verification_success),
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    uiState.eventName?.let { name ->
                        Text(text = stringResource(R.string.event_name, name))
                    }
                    uiState.userName?.let { name ->
                        Text(text = stringResource(R.string.user_name, name))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { onVerificationSuccess(token) }) {
                        Text(text = stringResource(R.string.continue_button))
                    }
                }
                uiState.isVerified == false -> {
                    Text(
                        text = stringResource(R.string.verification_failed),
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    uiState.errorMessage?.let { message ->
                        Text(text = message, textAlign = TextAlign.Center)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onVerificationFailed) {
                        Text(text = stringResource(R.string.go_back_button))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerificationScreenLoadingPreview() {
    OPassTheme {
        VerificationScreen(
            eventId = "testEvent",
            token = "testToken",
            onVerificationSuccess = {},
            onVerificationFailed = {},
            viewModel = hiltViewModel()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VerificationScreenSuccessPreview() {
    OPassTheme {
        VerificationScreen(
            eventId = "testEvent",
            token = "testToken",
            onVerificationSuccess = {},
            onVerificationFailed = {},
            viewModel = hiltViewModel()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VerificationScreenFailedPreview() {
    OPassTheme {
        VerificationScreen(
            eventId = "testEvent",
            token = "testToken",
            onVerificationSuccess = {},
            onVerificationFailed = {},
            viewModel = hiltViewModel()
        )
    }
}
