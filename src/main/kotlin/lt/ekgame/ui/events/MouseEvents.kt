package lt.ekgame.ui.events

import lt.ekgame.ui.Container
import lt.ekgame.ui.Element
import lt.ekgame.ui.units.Point

abstract class MouseEvent(
    val global: Point,
    val position: Point = global,
) : AbstractEvent() {
    abstract fun forPosition(position: Point): MouseEvent

    override fun forContext(element: Element): Event {
        if (element is Container && element.placeable.x != null && element.placeable.y != null) {
            return forPosition(position.subtract(element.placeable.x!!, element.placeable.y!!))
        }
        return this
    }

    override fun toString(): String {
        return "${this::class.simpleName ?: ""}[${position.x.toInt()},${position.y.toInt()}]"
    }
}

class MouseMoveEvent(
    global: Point,
    position: Point = global,
    override val parent: Event? = null,
) : MouseEvent(global, position) {
    override fun forPosition(position: Point) = MouseMoveEvent(global, position, this)
}

enum class MouseButton {
    LEFT, RIGHT, MIDDLE
}

class MouseClickedEvent(
    global: Point,
    position: Point = global,
    val button: MouseButton,
    override val parent: Event? = null,
) : MouseEvent(global, position) {
    override fun forPosition(position: Point) = MouseClickedEvent(global, position, button, this)
}