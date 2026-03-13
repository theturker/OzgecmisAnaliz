package com.alperenturker.cviews.util

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

actual fun putTextToClipboard(text: String) {
    Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(text), null)
}
