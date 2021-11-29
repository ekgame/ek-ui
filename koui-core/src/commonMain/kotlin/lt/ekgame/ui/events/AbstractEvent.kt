package lt.ekgame.ui.events

abstract class AbstractEvent(
    override val parent: Event? = null,
) : Event {
    final override var isPropagating: Boolean = true
        private set

    override fun stopPropagation() {
        isPropagating = false
        parent?.stopPropagation()
    }
}