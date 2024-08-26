/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.database

import app.cash.sqldelight.db.SqlDriver

internal expect object DriverFactory {
    fun createDriver(): SqlDriver
}
