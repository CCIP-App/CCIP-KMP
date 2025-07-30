/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.utils

import android.content.Context
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtil {

    private fun isCurrentYear(millis: Long): Boolean {
        val year = SimpleDateFormat("yyyy", Locale.getDefault())
            .format(Date(millis))

        val currentYear = SimpleDateFormat("yyyy", Locale.getDefault())
            .format(Date())

        return year == currentYear
    }

    fun formatShowDate(
         context: Context?,
         millis: Long
    ): String {
        val locale = Locale.getDefault()
        val isCurrentYear = isCurrentYear(millis)
        val pattern = when (locale.toLanguageTag()) {
            "nan-Hant-TW" -> {
                if (isCurrentYear) "M'月'd'日'" else "yyyy'年'M'月'd'日'"
            }

            "nan-Latn-TW-pehoeji" -> {
                if (isCurrentYear) "M 'goe̍h' d 'ji̍t'" else "yyyy 'nî' M 'goe̍h' d 'ji̍t'"
            }

            "nan-Latn-TW-tailo" -> {
                if (isCurrentYear) "M 'gue̍h' d 'ji̍t'" else "yyyy 'nî' M 'gue̍h' d 'ji̍t'"
            }

            else -> null
        }

        if (pattern != null) {
            return SimpleDateFormat(pattern, locale).format(Date(millis))
        }

        return DateUtils.formatDateTime(
            context,
            millis,
            DateUtils.FORMAT_SHOW_DATE
        )
    }
}
