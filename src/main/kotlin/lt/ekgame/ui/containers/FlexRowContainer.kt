package lt.ekgame.ui.containers

import lt.ekgame.ui.*
import lt.ekgame.ui.constraints.*
import lt.ekgame.ui.containers.helpers.FlexPlacementHelper
import java.awt.Color

open class FlexRowContainer(
    id: String = "",
    parent: Element?,
    size: SizeConstraints = SizeConstraints.DEFAULT,
    padding: PaddingValues = PaddingValues.ZERO,
    background: Color? = null,
    val gap: Float = 0f,
    val horizontalAlignment: Alignment = StartAlignment,
    val verticalAlignment: Alignment = StartAlignment,
    val verticalContainerAlignment: Alignment = StartAlignment,
) : GenericContainer(id, parent, size, padding, background) {

    private fun getTotalGapSize(): Float = (computedChildren.size - 1).coerceAtLeast(0)*gap

    private fun getTotalChildWidthWithGaps(): Float = getTotalChildWidth() + getTotalGapSize()

    private fun calculateFlex(elements: List<Placeable>): FlexPlacementHelper {
        requireNotNull(placeable.width) { "The placeable has to have defined width to apply flex." }
        val flexHelper = FlexPlacementHelper()
        elements.forEach { child ->
            val currentBucketWidth = flexHelper.getCurrentBucketWidth(gap)
            val childWidth = child.width ?: 0f
            if (currentBucketWidth + childWidth > getInnerWidth()) {
                flexHelper.addBucket()
            }
            flexHelper.add(child)
        }
        return flexHelper
    }

    override fun measure(container: Container?): Boolean {
        if (!super.measure(container) || !isValidPlaceable()) {
            return false
        }

        val availableWidth = placeable.width!! - this.padding.horizontal
        val availableHeight = placeable.height!! - this.padding.vertical

        val childPlaceables = getRemeasuredChildren(container)
        val flex = calculateFlex(childPlaceables)
        val flexHeight = flex.getTotalHeight(gap)

        var offsetX = 0f
        var offsetY = 0f

        (0 until flex.getNumBuckets()).forEach { bucketIndex ->
            val bucket = flex.getBucket(bucketIndex)
            val bucketWidth = flex.getBucketWidth(bucketIndex, gap)
            val bucketMaxHeight = flex.getBucketMaxHeight(bucketIndex)
            bucket.forEach {
                it.x = padding.left + offsetX + horizontalAlignment.calculate(bucketWidth, availableWidth)
                it.y = padding.top + offsetY + verticalAlignment.calculate(it.height!!, bucketMaxHeight) + verticalContainerAlignment.calculate(flexHeight, availableHeight)
                offsetX += it.width!! + gap
            }
            offsetX = 0f
            offsetY += bucketMaxHeight + gap
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
        placeable.height = applyMinMaxHeight(when {
            this.size.height is ContentSize && placeable.width != null -> {
                val flex = calculateFlex(computedChildren.asPlaceables())
                flex.getTotalHeight(gap) + this.padding.vertical
            }
            else -> placeable.height
        }, container)
    }
}