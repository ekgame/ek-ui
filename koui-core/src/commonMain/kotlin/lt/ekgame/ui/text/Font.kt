package lt.ekgame.ui.text

interface Font {
    fun measureWidth(text: String, textStyle: TextStyle): Float
    fun measureHeight(text: String, textStyle: TextStyle): Float

    fun withBold(): Font
    fun withItalic(): Font
}