package com.alperenturker.cviews.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.alperenturker.cviews.appContext

actual fun putTextToClipboard(text: String) {
    val ctx = appContext ?: return
    (ctx.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.setPrimaryClip(
        ClipData.newPlainText("cv_rehberi", text)
    )
}
