package lt.ekgame.ui.containers

import lt.ekgame.ui.*
import lt.ekgame.ui.constraints.*
import java.awt.Color

open class RowContainer(
    id: String = "",
    parent: Element?,
    size: SizeConstraints = SizeConstraints.DEFAULT,
    padding: PaddingValues = PaddingValues.ZERO,
    background: Color? = null,
    val gap: Float = 0f,
    val horizontalAlignment: Alignment = StartAlignment,
    val verticalAlignment: Alignment = StartAlignment,
) : GenericContainer(id, parent, size, padding, background) {

    override val horizontalFractionalSpace = object : FractionalSpace {
        override val totalFraction: Float
            get() = computedChildren.asSequence()
                .map { it.size.width }
                .filterIsInstance<FractionalSize>()
                .map { it.fraction }
                .sum()

        override val availableFractionalSpace: Float
            get() = getEffectiveWidth() - totalNonFractionalChildSize

        val totalNonFractionalChildSize: Float
            get() = computedChildren.filter { it.size.width !is FractionalSize }.mapNotNull { it.placeable.width }.sum()
    }

    override fun getEffectiveWidth(): Float = getInnerWidth() - getGapSize()

    private fun getGapSize(): Float = (computedChildren.size - 1).coerceAtLeast(0)*gap

    private fun getTotalChildWidthWithGaps(): Float = getTotalChildWidth() + getGapSize()

    override fun measure(container: Container?): Boolean {
        if (!super.measure(container) || !isValidPlaceable()) {
            return false
        }

        val availableWidth = placeable.width!! - this.padding.horizontal
        val availableHeight = placeable.height!! - this.padding.vertical

        val childPlaceables = getRemeasuredChildren(container)
        val totalChildWidth = getTotalChildWidthWithGaps()

        var offset = 0f
        for (child in childPlaceables) {
            child.apply {
                x = padding.left + offset + horizontalAlignment.calculate(totalChildWidth, availableWidth)
                y = padding.top + verticalAlignment.calculate(height!!, availableHeight)
                offset += width!! + gap
            }
        }

        return true
    }

    override fun recalculateContainerWidth(container: Container?) {
        placeable.width = applyMinMaxWidth(when (this.size.width) {
            is ContentSize -> getTotalChildWidthWithGaps() + this.padding.horizontal
            else -> placeable.width
        }, container)
    }

    override fun recalculateContainerHeight(container: Container?) {
        placeable.height = applyMinMaxHeight(when (this.size.height) {
            is ContentSize -> getMaxChildHeight() + this.padding.vertical
            else -> placeable.height
        }, container)
    }
}