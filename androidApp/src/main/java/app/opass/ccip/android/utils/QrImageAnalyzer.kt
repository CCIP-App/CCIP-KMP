/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.utils

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.google.zxing.client.result.ResultParser
import zxingcpp.BarcodeReader

class QrImageAnalyzer(onTokenFound: (token: String) -> Unit = {}): ImageAnalysis.Analyzer {

    private val reader = BarcodeReader().apply {
        options.tryRotate = true
    }

    override fun analyze(image: ImageProxy) {
        image.use {
            reader.read(image).firstOrNull()?.let { rawResult ->
                if (rawResult.format != BarcodeReader.Format.QR_CODE || rawResult.text.isNullOrBlank()) return

                val result = Result(
                    rawResult.text,
                    rawResult.bytes,
                    null,
                    BarcodeFormat.QR_CODE
                )
                val parsedResult = ResultParser.parseResult(result)
                Log.d("AAYUSH", "TOKEN: ${parsedResult.displayResult}, ${rawResult.text}")
            }
        }
    }
}
