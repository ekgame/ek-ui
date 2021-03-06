package lt.ekgame.ui.events

import dev.romainguy.kotlin.math.Float4
import dev.romainguy.kotlin.math.inverse
import lt.ekgame.ui.Container
import lt.ekgame.ui.Element
import lt.ekgame.ui.units.Point

abstract class InternalMouseEvent(
    val screenPosition: Point,
    val relativePosition: Point = screenPosition,
) : AbstractEvent() {
    abstract fun forPosition(position: Point): InternalMouseEvent

    override fun forContext(element: Element, direction: EventDirection): Event {
        if (element.placeable.x != null && element.placeable.y != null) {
            val vector = Float4(relativePosition.x, relativePosition.y, 1f, 1f)
            val newRelativePosition = when (direction) {
                EventDirection.UP -> {
                    val newVector = inverse(element.transformMatrix).times(vector)
                    Point(newVector.x, newVector.y)
                }
                EventDirection.DOWN -> {
                    val newVector = element.transformMatrix.times(vector)
                    Point(newVector.x, newVector.y)
                }
            }
            return forPosition(newRelativePosition)
        }
        return this
    }

    override fun toString(): String {
        return "${this::class.simpleName ?: ""}[${relativePosition.x.toInt()},${relativePosition.y.toInt()}]"
    }
}

class InternalMouseMoveEvent(
    global: Point,
    position: Point = global,
    isHovered: Boolean = false,
    override val parent: InternalMouseMoveEvent? = null,
) : InternalMouseEvent(global, position) {
    var hoverHandled: Boolean = isHovered
        private set

    var isChildHovered = false
        private set

    override fun forPosition(position: Point) = InternalMouseMoveEvent(screenPosition, position, hoverHandled, this)

    fun setHoverAsHandled() {
        hoverHandled = true
        isChildHovered = true
        parent?.setHoverAsHandled()
    }
}

enum class MouseButton {
    LEFT, RIGHT, MIDDLE
}

class InternalMouseClickedEvent(
    global: Point,
    position: Point = global,
    val button: MouseButton,
    override val parent: Event? = null,
) : InternalMouseEvent(global, position) {
    override fun forPosition(position: Point) = InternalMouseClickedEvent(screenPosition, position, button, this)
}