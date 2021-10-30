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
    }
}
