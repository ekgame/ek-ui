package lt.ekgame.ui.events

interface EventListener<T : Event> {
    fun callback(event: T)
}