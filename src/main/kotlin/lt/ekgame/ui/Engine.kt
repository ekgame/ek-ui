package lt.ekgame.ui

import lt.ekgame.ui.constraints.PaddingValues
import lt.ekgame.ui.constraints.SizeConstraints
import lt.ekgame.ui.events.Event
import lt.ekgame.ui.events.EventListener
import kotlin.reflect.KClass

interface Placeable {
    val element: Element
    var x: Float?
    var y: Float?
    var width: Float?
    var height: Float?

    fun reset() {
        resetPosition()
        resetSize()
    }

    fun resetPosition() {
        x = null
        y = null
    }

    fun resetSize() {
        width = null
        height = null
    }
}

interface Element {
    val id: String
    val parent: Container?
    val size: SizeConstraints
    val placeable: Placeable

    fun measure(container: Container?): Boolean

    fun propagateEvent(event: Event)
    fun <T : Event>listen(clazz: KClass<T>, listener: EventListener<Event>)
}

inline fun <reified T : Event> Element.listen(crossinline callback: (T) -> Unit) {
    listen(T::class, object : EventListener<Event> {
        override fun callback(event: Event) {
            callback.invoke(event as T)
        }
    })
}

interface Container : Element {
    val children: List<Element>
    val computedChildren: List<Element>
        get() = children
    val padding: PaddingValues
    val verticalFractionalSpace: FractionalSpace?
    val horizontalFractionalSpace: FractionalSpace?

    fun addChild(element: Element)
    fun addAllChildren(vararg elements: Element) = elements.forEach(::addChild)
    fun removeChild(element: Element)
    fun removeAllChildren(vararg elements: Element) = elements.forEach(::removeChild)

    fun getInnerWidth() = requireNotNull(placeable.width) - padding.horizontal
    fun getEffectiveWidth() = getInnerWidth()
    fun getInnerHeight() = requireNotNull(placeable.height) - padding.vertical
    fun getEffectiveHeight() = getInnerHeight()
}

interface FractionalSpace {
    val totalFraction: Float
    val availableFractionalSpace: Float
}

class BasicPlaceable(
    override val element: Element,
    override var x: Float? = null,
    override var y: Float? = null,
    override var width: Float? = null,
    override var height: Float? = null,
) : Placeable
