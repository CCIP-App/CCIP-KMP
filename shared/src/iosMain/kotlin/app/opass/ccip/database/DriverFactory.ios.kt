/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.database

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.opass.ccip.database.OPassDatabaseHelper.Companion.FILE_NAME

internal actual object DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(OPassDatabase.Schema.synchronous(), FILE_NAME)
    }
}
