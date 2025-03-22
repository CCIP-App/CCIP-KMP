/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import zxingcpp.BarcodeReader
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun providesSDFInstance(): SimpleDateFormat {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault())
    }

    @Provides
    @Singleton
    fun provideBarcodeReaderInstance(): BarcodeReader {
        return BarcodeReader().apply {
            options.tryRotate = true
            options.formats = setOf(BarcodeReader.Format.QR_CODE)
        }
    }

    @Provides
    @Singleton
    fun providesBackgroundExecutor(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }
}
