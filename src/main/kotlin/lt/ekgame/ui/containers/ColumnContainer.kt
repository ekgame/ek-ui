package lt.ekgame.ui.containers

import lt.ekgame.ui.*
import lt.ekgame.ui.constraints.*
import java.awt.Color

open class ColumnContainer(
    id: String = "",
    parent: Container?,
    size: SizeConstraints = SizeConstraints.DEFAULT,
    padding: PaddingValues = PaddingValues.ZERO,
    background: Color? = null,
    var gap: Float = 0f,
    var horizontalAlignment: Alignment = StartAlignment,
    var verticalAlignment: Alignment = StartAlignment,
) : GenericContainer(id, parent, size, padding, background) {

    override val verticalFractionalSpace = object : FractionalSpace {
        override val totalFraction: Float
            get() = computedChildren.asSequence()
                .map { it.size.height }
                .filterIsInstance<FractionalSize>()
                .map { it.fraction }
                .sum()

        override val availableFractionalSpace: Float
            get() = getEffectiveHeight() - totalNonFractionalChildSize

        val totalNonFractionalChildSize: Float
            get() = computedChildren.filter { it.size.height !is FractionalSize }.mapNotNull { it.placeable.height }.sum()
    }

    override fun getEffectiveHeight(): Float = getInnerHeight() - getGapSize()

    private fun getGapSize(): Float = (children.size - 1).coerceAtLeast(0)*gap

    private fun getTotalChildHeightWithGaps(): Float = getTotalChildHeight() + getGapSize()

    override fun measure(container: Container?): Boolean {
        if (!super.measure(container) || !isValidPlaceable()) {
            return false
        }

        val availableWidth = placeable.width!! - this.padding.horizontal
        val availableHeight = placeable.height!! - this.padding.vertical

        val childPlaceables = getRemeasuredChildren(container)
        val totalChildHeight = getTotalChildHeightWithGaps()

        var offset = 0f
        for (child in childPlaceables) {
            child.apply {
                x = padding.left + horizontalAlignment.calculate(width!!, availableWidth)
                y = padding.top + offset + verticalAlignment.calculate(totalChildHeight, availableHeight)
                offset += height!! + gap
            }
        }

        return true
    }

    override fun recalculateContainerWidth(container: Container?) {
        placeable.width = applyMinMaxWidth(when (this.size.width) {
            is ContentSize -> getMaxChildWidth() + this.padding.horizontal
            else -> placeable.width
        }, container)
    }

    override fun recalculateContainerHeight(container: Container?) {
        placeable.height = applyMinMaxHeight(when (this.size.height) {
            is ContentSize -> getTotalChildHeightWithGaps() + this.padding.vertical
            else -> placeable.height
        }, container)
    }
}