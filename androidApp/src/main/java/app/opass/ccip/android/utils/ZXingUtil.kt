/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter

object ZXingUtil {

    private const val BITMAP_QR_HEIGHT = 512
    private const val BITMAP_QR_WIDTH = 512

    fun generateQR(token: String): Bitmap {
        val bitMatrix = MultiFormatWriter()
            .encode(token, BarcodeFormat.QR_CODE, BITMAP_QR_WIDTH, BITMAP_QR_HEIGHT)

        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
            }
        }

        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    }
}
