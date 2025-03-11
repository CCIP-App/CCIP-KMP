package app.opass.ccip.android.ui.models

import androidx.annotation.StringRes

data class LanguageOptionItem(
    @StringRes
    val localNameResId: Int,
    @StringRes
    val translatedNameResId: Int
)
