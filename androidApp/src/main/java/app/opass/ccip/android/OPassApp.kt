/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android

import android.app.Application
import app.opass.ccip.android.utils.NotificationUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OPassApp: Application() {

    override fun onCreate() {
        super.onCreate()
        NotificationUtil.createNotificationChannels(this)
    }
}
