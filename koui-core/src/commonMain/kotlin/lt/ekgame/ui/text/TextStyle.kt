package lt.ekgame.ui.text

import com.github.ajalt.colormath.Color
import com.github.ajalt.colormath.model.RGB

data class TextStyle(
    var font: Font,
    var color: Color = RGB("#000000"),
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