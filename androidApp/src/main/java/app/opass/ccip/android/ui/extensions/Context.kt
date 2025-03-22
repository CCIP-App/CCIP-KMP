/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.extensions

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

private const val TAG = "Context"

val Context.sharedPreferences: SharedPreferences
    get() = this.getSharedPreferences(
        "shared_preferences",
        Context.MODE_PRIVATE
    )

/**
 * Opens given URL in a custom tab for the user to browse
 * @param url URL to open
 */
fun Context.browse(url: String) {
    try {
        val customTabsIntent = CustomTabsIntent.Builder()
        customTabsIntent.build().launchUrl(this, url.toUri())
    } catch (exception: Exception) {
        Log.e(TAG, "Failed to open custom tab", exception)
    }
}

/**
 * Makes a short-length toast
 * @param resId String resource ID of the toast content
 */
fun Context.toast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}
