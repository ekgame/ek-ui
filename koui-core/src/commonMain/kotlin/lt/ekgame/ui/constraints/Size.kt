package lt.ekgame.ui.constraints

sealed class Size
open class AbsoluteSize(val size: Float) : Size()

open class PercentageSize(val percetage: Float) : Size() {
    init {
        require(percetage > 0) { "Percentage has to be positive." }
    }
}

open class FractionalSize(val fraction: Float) : Size() {
    init {
        require(fraction > 0) { "Fraction has to be positive." }
    }
}

object ZeroSize : AbsoluteSize(0f)
object MaxAvailable : FractionalSize(1f)
object ContentSize : Size()

open class SizeConstraints(
    open val width: Size = MaxAvailable,
    open val height: Size = MaxAvailable,
    open val minWidth: Size = ContentSize,
    open val minHeight: Size = ContentSize,
    open val maxWidth: Size = MaxAvailable,
    open val maxHeight: Size = MaxAvailable,
) {
    companion object {
        val DEFAULT = SizeConstraints()
        val CONTENT = SizeConstraints(ContentSize, ContentSize)
    }
}

val Int.dp: Size
    get() = AbsoluteSize(this.toFloat())