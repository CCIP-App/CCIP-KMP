/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.PendingIntentCompat
import androidx.core.content.getSystemService
import app.opass.ccip.android.MainActivity
import app.opass.ccip.android.R
import app.opass.ccip.android.utils.AlarmUtil.INTENT_EXTRA_EVENT_ID
import app.opass.ccip.android.utils.AlarmUtil.INTENT_EXTRA_SESSION_ID

object NotificationUtil {

    private const val SESSION_CHANNEL_ID = "session"

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannels(context: Context) {
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

    fun getSessionNotification(
        context: Context,
        eventId: String,
        sessionId: String,
        title: String,
        room: String
    ): Notification {
        val newIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(INTENT_EXTRA_EVENT_ID, eventId)
            putExtra(INTENT_EXTRA_SESSION_ID, sessionId)
        }
        val pendingIntent = PendingIntentCompat.getActivity(context, 0, newIntent, 0, false)

        val content = context.getString(R.string.notification_session_start_message, title, room)

        return NotificationCompat.Builder(context, SESSION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bookmark)
            .setContentTitle(context.getString(R.string.notification_session_start_title))
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(pendingIntent)
            .build()
    }
}
