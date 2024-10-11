/*
 * SPDX-FileCopyrightText: 2024 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.utils

import android.graphics.Rect
import android.os.Parcel
import android.text.Spannable
import android.text.Spanned
import android.text.method.TransformationMethod
import android.text.style.URLSpan
import android.text.util.Linkify
import android.util.Log
import android.view.View
import android.widget.TextView
import app.opass.ccip.android.ui.extensions.browse

open class LinkTransformationMethod : TransformationMethod {

    private val TAG = LinkTransformationMethod::class.java.simpleName

    inner class CustomTabsURLSpan : URLSpan {
        constructor(url: String?) : super(url)
        constructor(src: Parcel?) : super(src!!)

        override fun onClick(widget: View) {
            try {
                widget.context.browse(url)
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to open link in custom tab!", exception)
                super.onClick(widget)
            }
        }
    }

    override fun getTransformation(source: CharSequence?, view: View?): CharSequence? {
        if (view is TextView) {
            Linkify.addLinks(view, Linkify.WEB_URLS)
            if (view.text == null || view.text !is Spannable) {
                return source
            }
            val text = view.text as Spannable
            val spans = text.getSpans(0, view.length(), URLSpan::class.java)
            spans.indices.reversed().forEach {
                val oldSpan = spans[it]
                val start = text.getSpanStart(oldSpan)
                val end = text.getSpanEnd(oldSpan)
                val url = oldSpan.url
                text.removeSpan(oldSpan)
                text.setSpan(CustomTabsURLSpan(url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            return text
        }
        return source
    }

    override fun onFocusChanged(
        view: View?,
        sourceText: CharSequence?,
        focused: Boolean,
        direction: Int,
        previouslyFocusedRect: Rect?
    ) {}
}
