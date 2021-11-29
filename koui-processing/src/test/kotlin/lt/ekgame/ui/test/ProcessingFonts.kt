package lt.ekgame.ui.test

import lt.ekgame.ui.text.Font
import lt.ekgame.ui.text.TextStyle
import processing.core.PApplet
import processing.core.PFont

class ProcessingFont(
    val applet: PApplet,
    val nativeFont: PFont,
) : Font {

    lateinit var boldVersion: Font
    lateinit var italicVersion: Font

    override fun withBold(): Font = boldVersion

    override fun withItalic(): Font = italicVersion

    override fun measureWidth(text: String, textStyle: TextStyle): Float {
        applet.textFont(nativeFont)
        applet.textSize(textStyle.size)
        return applet.textWidth(text)
    }

    override fun measureHeight(text: String, textStyle: TextStyle): Float {
        applet.textFont(nativeFont)
        applet.textSize(textStyle.size)
        return applet.textAscent() + applet.textDescent()
    }
}

fun buildFont(
    applet: PApplet,
    normal: String,
    bold: String,
    italic: String,
    boldItalic: String,
): Font {
    val normalFont = ProcessingFont(applet, applet.createFont(normal, 12f))
    val boldFont = ProcessingFont(applet, applet.createFont(bold, 12f))
    val italicFont = ProcessingFont(applet, applet.createFont(italic, 12f))
    val boldItalicFont = ProcessingFont(applet, applet.createFont(boldItalic, 12f))

    normalFont.boldVersion = boldFont
    normalFont.italicVersion = italicFont

    boldFont.boldVersion = boldFont
    boldFont.italicVersion = boldItalicFont

    italicFont.boldVersion = boldItalicFont
    italicFont.italicVersion = italicFont

    boldItalicFont.boldVersion = boldItalicFont
    boldItalicFont.italicVersion = boldItalicFont

    return normalFont
}

fun buildFont(
    applet: PApplet,
    normal: String,
) = buildFont(applet, normal, "$normal Bold", "$normal Italic", "$normal Bold Italic")