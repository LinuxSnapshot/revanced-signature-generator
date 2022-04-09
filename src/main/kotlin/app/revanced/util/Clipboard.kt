package app.revanced.util

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

object Clipboard {
    internal fun saveContent(content: String) {
        val selection = StringSelection(content)
        Toolkit.getDefaultToolkit().systemClipboard.setContents(selection, selection)
    }
}