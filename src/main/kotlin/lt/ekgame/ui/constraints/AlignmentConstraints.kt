package lt.ekgame.ui.constraints

sealed class Alignment {
    abstract fun calculate(elementSize: Float, totalSpace: Float): Float
}

open class RelativeAlignment(private val position: Float) : Alignment() {
    override fun calculate(elementSize: Float, totalSpace: Float): Float = position*(totalSpace - elementSize)
}

object StartAlignment : RelativeAlignment(0f)

object CenterAlignment : RelativeAlignment(0.5f)

object EndAlignment : RelativeAlignment(1f)