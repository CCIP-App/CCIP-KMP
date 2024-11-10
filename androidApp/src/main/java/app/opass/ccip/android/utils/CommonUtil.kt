/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.utils

import android.app.Activity
import android.content.Context
import android.view.WindowManager

object CommonUtil {

    fun setBrightness(context: Context, isFull: Boolean) {
        val activity = context as? Activity ?: return
        val layoutParams: WindowManager.LayoutParams = activity.window.attributes
        layoutParams.screenBrightness =
            if (isFull) 1.0F else WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        activity.window.attributes = layoutParams
    }
}
