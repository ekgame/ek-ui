package lt.ekgame.ui.units

data class Point(val x: Float, val y: Float) {
    fun add(x: Float, y: Float) = Point(this.x + x, this.y + y)
    fun subtract(x: Float, y: Float) = Point(this.x - x, this.y - y)
}