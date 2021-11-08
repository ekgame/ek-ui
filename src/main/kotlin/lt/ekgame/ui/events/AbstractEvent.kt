package lt.ekgame.ui.events

abstract class AbstractEvent : Event {
    final override var isPropagating: Boolean = true
        private set

    override fun stopPropagation() {
        isPropagating = false
    }
}