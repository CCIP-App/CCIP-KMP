/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package utils

import android.content.Context
import androidx.startup.Initializer

object AppContextWrapper {
    var appContext: Context? = null
}

// Used in the Android Manifest
internal class ContextInitializer : Initializer<Context> {
    private var appContext: Context? = null

    override fun create(context: Context): Context =
        context.applicationContext.also {
            appContext = it
            AppContextWrapper.appContext = it
        }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
