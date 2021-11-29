package lt.ekgame.ui.events

import lt.ekgame.ui.Element
import lt.ekgame.ui.units.Point

class MouseClickedEvent(
    val relativePosition: Point,
    val screenPosition: Point,
    val button: MouseButton,
) : AbstractEvent()

class PointerEnteredEvent(
    val target: Element,
) : AbstractEvent()

class PointerLeftEvent(
    val target: Element,
) : AbstractEvent()