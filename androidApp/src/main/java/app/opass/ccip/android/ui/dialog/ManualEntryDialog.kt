/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.opass.ccip.android.R
import kotlinx.coroutines.android.awaitFrame

/**
 * Dialog for entering the token manually
 * @param onConfirm Callback when the token has been entered
 * @param onDismiss Callback when the dialog has been dismissed
 */
@Composable
fun ManualEntryDialog(onConfirm: (token: String?) -> Unit = {}, onDismiss: () -> Unit = {}) {
    var token by rememberSaveable { mutableStateOf<String?>(null) }
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
                    value = token ?: String(),
                    onValueChange = { token = it.ifBlank { null } },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }
        },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton (
                onClick = { onConfirm(token) },
                enabled = !token.isNullOrBlank()
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

@Preview
@Composable
private fun ManualEntryDialogPreview() {
    ManualEntryDialog()
}
