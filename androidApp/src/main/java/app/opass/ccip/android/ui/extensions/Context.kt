/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.extensions

import android.content.Context
import android.content.SharedPreferences

val Context.sharedPreferences: SharedPreferences
    get() = this.getSharedPreferences(
        "shared_preferences",
        Context.MODE_PRIVATE
    )
