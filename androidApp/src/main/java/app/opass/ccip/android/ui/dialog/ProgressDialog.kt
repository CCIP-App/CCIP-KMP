/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.dialog

import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * Dialog to show when some progress is ongoing
 * @param onDismiss Callback when the dialog is dismissed
 */
@Composable
fun ProgressDialog(onDismiss: () -> Unit = {}) {
    Dialog(onDismissRequest = { onDismiss() }) {
        CircularProgressIndicator(modifier = Modifier.requiredWidth(48.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressDialogPreview() {
    ProgressDialog()
}
