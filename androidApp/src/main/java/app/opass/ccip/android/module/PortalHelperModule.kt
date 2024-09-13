/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.module

import app.opass.ccip.helpers.PortalHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PortalHelperModule {

    @Provides
    @Singleton
    fun providesPortalHelperInstance(): PortalHelper {
        return PortalHelper()
    }
}
