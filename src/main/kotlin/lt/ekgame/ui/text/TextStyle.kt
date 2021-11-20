package lt.ekgame.ui.text

import java.awt.Color

data class TextStyle(
    var font: Font,
    var color: Color = Color.BLACK,
    var size: Float = 12f,
    var bold: Boolean = false,
    var italic: Boolean = false,
    var underline: Boolean = false,
) {
    val computedFont by lazy {
        var current = font
        if (bold) {
            current = current.withBold()
        }
        if (italic) {
            current = current.withItalic()
        }
        current
    }
}