/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.extensions

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent

private const val TAG = "Context"

val Context.sharedPreferences: SharedPreferences
    get() = this.getSharedPreferences(
        "shared_preferences",
        Context.MODE_PRIVATE
    )

fun Context.browse(url: String) {
    try {
        val customTabsIntent = CustomTabsIntent.Builder()
        customTabsIntent.build().launchUrl(this, Uri.parse(url))
    } catch (exception: Exception) {
        Log.e(TAG, "Failed to open custom tab", exception)
    }
}
