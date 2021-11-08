package lt.ekgame.ui.events

import lt.ekgame.ui.Element

interface Event {
    val parent: Event?
    val isPropagating: Boolean
    fun stopPropagation()
    fun forContext(element: Element): Event = this
}
