/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.getSystemService
import app.opass.ccip.android.R

object NotificationUtil {

    private const val SESSION_CHANNEL_ID = "session"

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService<NotificationManager>()!!
            val channels = ArrayList<NotificationChannel>().apply {
                add(
                    NotificationChannel(
                        SESSION_CHANNEL_ID,
                        context.getString(R.string.session),
                        NotificationManager.IMPORTANCE_HIGH
                    )
                )
            }

            notificationManager.createNotificationChannels(channels)
        }
    }
}
