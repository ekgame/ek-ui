package lt.ekgame.ui.containers

import lt.ekgame.ui.elements.AbstractElement
import lt.ekgame.ui.Container
import lt.ekgame.ui.Element
import lt.ekgame.ui.FractionalSpace
import lt.ekgame.ui.Placeable
import lt.ekgame.ui.constraints.ContentSize
import lt.ekgame.ui.constraints.PaddingValues
import lt.ekgame.ui.constraints.SizeConstraints
import processing.core.PApplet

abstract class AbstractContainer(
    override val parent: Element? = null,
    override val size: SizeConstraints = SizeConstraints.DEFAULT,
    override val padding: PaddingValues = PaddingValues.ZERO,
) : AbstractElement(parent, size), Container {
    override val children: MutableList<Element> = mutableListOf()

    override val verticalFractionalSpace: FractionalSpace? = null

    override val horizontalFractionalSpace: FractionalSpace? = null

    protected fun getMaxChildWidth() = children.mapNotNull { it.placeable.width }.maxOrNull() ?: 0f

    protected fun getMaxChildHeight() = children.mapNotNull { it.placeable.height }.maxOrNull() ?: 0f

    protected fun getTotalChildWidth() = children.mapNotNull { it.placeable.width }.sum()

    protected fun getTotalChildHeight() = children.mapNotNull { it.placeable.height }.sum()

    protected fun getRemeasuredChildren(container: Container?): List<Placeable> = children
        .asSequence()
        .map {
            if (it.placeable.width == null || it.placeable.height == null) {
                it.measure(this)
            }
            it.placeable
        }
        .filter { it.width != null && it.height != null }
        .toList().also {
            recalculateSize(container)
        }

    override fun measure(container: Container?): Boolean {
        children.forEach {
            it.placeable.reset()
            it.measure(this)
        }

        return super.measure(container).also {
            recalculateSize(container)
        }
    }

    override fun addChild(element: Element) {
        children.add(element)
    }

    override fun removeChild(element: Element) {
        children.remove(element)
    }

    protected fun isValidPlaceable() = placeable.width != null && placeable.height != null

    open fun recalculateSize(container: Container?) {
        if (placeable.element is RootContainer) {
            return
        }
        recalculateContainerWidth(container)
        recalculateContainerHeight(container)
        // In case width depends on the height and the height was not calculated for the first call
        recalculateContainerWidth(container)
    }

    open fun recalculateContainerWidth(container: Container?) {
        placeable.width = applyMinMaxWidth(when (this.size.width) {
            is ContentSize -> getMaxChildWidth() + this.padding.horizontal
            else -> placeable.width
        }, container)
    }

    open fun recalculateContainerHeight(container: Container?) {
        placeable.height = applyMinMaxHeight(when (this.size.height) {
            is ContentSize -> getMaxChildHeight() + this.padding.vertical
            else -> placeable.height
        }, container)
    }
}