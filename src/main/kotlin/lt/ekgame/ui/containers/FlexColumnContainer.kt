package lt.ekgame.ui.containers

import lt.ekgame.ui.*
import lt.ekgame.ui.constraints.*
import lt.ekgame.ui.containers.helpers.FlexPlacementHelper
import java.awt.Color

open class FlexColumnContainer(
    parent: Element?,
    size: SizeConstraints = SizeConstraints.DEFAULT,
    padding: PaddingValues = PaddingValues.ZERO,
    background: Color? = null,
    val gap: Float = 0f,
    val horizontalAlignment: Alignment = StartAlignment,
    val horizontalContainerAlignment: Alignment = StartAlignment,
    val verticalAlignment: Alignment = StartAlignment,
) : GenericContainer(parent, size, padding, background) {

    private fun getTotalGapSize(): Float = (children.size - 1).coerceAtLeast(0)*gap

    private fun getTotalChildHeightWithGaps(): Float = getTotalChildHeight() + getTotalGapSize()

    private fun calculateFlex(elements: List<Placeable>): FlexPlacementHelper {
        requireNotNull(placeable.height) { "The placeable has to have defined height to apply flex." }
        val flexHelper = FlexPlacementHelper()
        elements.forEach { child ->
            val currentBucketHeight = flexHelper.getCurrentBucketHeight(gap)
            val childHeight = child.height ?: 0f
            if (currentBucketHeight + childHeight > getInnerHeight()) {
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
        val flexWidth = flex.getTotalWidth(gap)

        var offsetX = 0f
        var offsetY = 0f

        (0 until flex.getNumBuckets()).forEach { bucketIndex ->
            val bucket = flex.getBucket(bucketIndex)
            val bucketHeight = flex.getBucketHeight(bucketIndex, gap)
            val bucketMaxWidth = flex.getBucketMaxWidth(bucketIndex)
            bucket.forEach {
                it.x = padding.left + offsetX + horizontalAlignment.calculate(it.width!!, bucketMaxWidth) + horizontalContainerAlignment.calculate(flexWidth, availableWidth)
                it.y = padding.top + offsetY + verticalAlignment.calculate(bucketHeight, availableHeight)
                offsetY += it.height!! + gap
            }
            offsetY = 0f
            offsetX += bucketMaxWidth + gap
        }

        return true
    }

    override fun recalculateContainerWidth(container: Container?) {
        placeable.width = applyMinMaxWidth(when {
            this.size.width is ContentSize && placeable.height != null -> {
                val flex = calculateFlex(children.asPlaceables())
                flex.getTotalWidth(gap) + this.padding.horizontal
            }
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