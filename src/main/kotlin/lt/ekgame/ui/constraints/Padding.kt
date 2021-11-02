package lt.ekgame.ui.constraints

data class PaddingValues(
    val top: Float = 0f,
    val right: Float = 0f,
    val bottom: Float = 0f,
    val left: Float = 0f,
) {
    val vertical: Float
        get() = top + bottom

    val horizontal: Float
        get() = left + right

    companion object {
        val ZERO = of(0f)
        fun of(size: Float) = PaddingValues(size, size, size, size)
        fun of(vertical: Float, horizontal: Float) = PaddingValues(vertical, horizontal, vertical, horizontal)
        fun of(top: Float, right: Float, bottom: Float, left: Float) = PaddingValues(top, right, bottom, left)

        fun vertical(vertical: Float) = PaddingValues(vertical, 0f, vertical, 0f)
        fun horizontal(horizontal: Float) = PaddingValues(0f, horizontal, 0f, horizontal)

        fun vertical(top: Float, bottom: Float) = PaddingValues(top, 0f, bottom, 0f)
        fun horizontal(left: Float, right: Float) = PaddingValues(0f, right, 0f, left)

        fun top(top: Float) = PaddingValues(top, 0f, 0f, 0f)
        fun right(right: Float) = PaddingValues(0f, right, 0f, 0f)
        fun bottom(bottom: Float) = PaddingValues(0f, 0f, bottom, 0f)
        fun left(left: Float) = PaddingValues(0f, 0f, 0f, left)
    }
}
