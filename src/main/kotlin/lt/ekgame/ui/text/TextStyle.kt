package lt.ekgame.ui.text

import java.awt.Color

data class TextStyle(
    val font: Font,
    val color: Color = Color.BLACK,
    val size: Float = 12f,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val underline: Boolean = false,
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