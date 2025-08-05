/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.extensions

import android.app.Activity
import android.view.WindowManager

/**
 * Overrides the activity's brightness level
 * @param isFull True for overriding the brightness to max, false to reset to default
 */
fun Activity.overrideBrightness(isFull: Boolean) {
    val layoutParams: WindowManager.LayoutParams = window.attributes
    layoutParams.screenBrightness =
        if (isFull) 1.0F else WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
    window.attributes = layoutParams
}
