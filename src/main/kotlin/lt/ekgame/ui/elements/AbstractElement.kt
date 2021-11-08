package lt.ekgame.ui.elements

import lt.ekgame.ui.*
import lt.ekgame.ui.constraints.*
import lt.ekgame.ui.events.Event
import lt.ekgame.ui.events.EventListener
import kotlin.reflect.KClass

abstract class AbstractElement(
    override val id: String,
    override val parent: Container?,
    override val size: SizeConstraints = SizeConstraints.DEFAULT,
) : Element {

    override val placeable: Placeable by lazy { BasicPlaceable(this) }

    private val listeners = mutableMapOf<KClass<*>, MutableList<EventListener<Event>>>()

    override fun <T : Event> listen(clazz: KClass<T>, listener: EventListener<Event>) {
        listeners
            .getOrPut(clazz) { mutableListOf() }
            .add(listener)
    }

    override fun propagateEvent(event: Event) {
        handleEvent(event.forContext(this))
    }

    fun handleEvent(event: Event) {
        listeners.asSequence()
            .filter { it.key.isInstance(event) }
            .flatMap { it.value }
            .firstOrNull {
                it.callback(event)
                !event.isPropagating
            }
    }


    override fun measure(container: Container?): Boolean {
        if (container == null) {
            return placeable.width != null && placeable.height != null
        }

        val computedWidth = computeWidth(container, size.width)
        val computedHeight = computeHeight(container, size.height)

        placeable.apply {
            width = applyMinMaxWidth(computedWidth, container)
            height = applyMinMaxHeight(computedHeight, container)
        }

        return true
    }

    protected fun applyMinMaxWidth(width: Float?, container: Container?): Float? {
        if (width != null && size.width != size.minWidth) {
            val computedMinWidth = computeWidth(container, size.minWidth)
            if (computedMinWidth != null && width < computedMinWidth) {
                return computedMinWidth
            }
        }

        if (width != null && size.width != size.maxWidth) {
            val computedMaxWidth = computeWidth(container, size.maxWidth)
            if (computedMaxWidth != null && width > computedMaxWidth) {
                return computedMaxWidth
            }
        }

        return width
    }

    protected fun applyMinMaxHeight(height: Float?, container: Container?): Float? {
        if (height != null && size.height != size.minHeight) {
            val computedMinHeight = computeHeight(container, size.minHeight)
            if (computedMinHeight != null && height < computedMinHeight) {
                return computedMinHeight
            }
        }

        if (height != null && size.height != size.maxHeight) {
            val computedMaxHeight = computeHeight(container, size.maxHeight)
            if (computedMaxHeight != null && height > computedMaxHeight) {
                return computedMaxHeight
            }
        }

        return height
    }

    protected open fun computeWidth(container: Container?, constraint: Size): Float?  = when {
        constraint is AbsoluteSize -> constraint.size
        container != null && container.placeable.width != null -> when {
            constraint is PercentageSize -> {
                container.getEffectiveWidth() * constraint.percetage
            }
            constraint is FractionalSize && container.horizontalFractionalSpace == null -> {
                container.getEffectiveWidth()
            }
            constraint is FractionalSize && container.horizontalFractionalSpace != null -> {
                val fraction = constraint.fraction / container.horizontalFractionalSpace!!.totalFraction
                container.horizontalFractionalSpace!!.availableFractionalSpace * fraction
            }
            else -> null
        }
        else -> null
    }

    protected open fun computeHeight(container: Container?, constraint: Size): Float? = when {
        constraint is AbsoluteSize -> constraint.size
        container != null && container.placeable.height != null -> when {
            constraint is PercentageSize-> {
                container.getEffectiveHeight()*constraint.percetage
            }
            constraint is FractionalSize && container.verticalFractionalSpace == null -> {
                container.getEffectiveHeight()
            }
            constraint is FractionalSize && container.verticalFractionalSpace != null -> {
                val fraction = constraint.fraction / container.verticalFractionalSpace!!.totalFraction
                container.verticalFractionalSpace!!.availableFractionalSpace * fraction
            }
            else -> null
        }
        else -> null
    }
}
