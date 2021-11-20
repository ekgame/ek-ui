package lt.ekgame.ui.containers

import lt.ekgame.ui.Container
import lt.ekgame.ui.Element
import lt.ekgame.ui.Placeable
import lt.ekgame.ui.asPlaceables
import lt.ekgame.ui.constraints.*
import lt.ekgame.ui.containers.helpers.FlexPlacementHelper
import lt.ekgame.ui.elements.TextLineElement
import lt.ekgame.ui.text.SplitResult
import lt.ekgame.ui.text.WrappingElement
import java.awt.Color

class TextContainer(
    id: String = "",
    parent: Container?,
    size: SizeConstraints = SizeConstraints.CONTENT,
    padding: PaddingValues = PaddingValues.ZERO,
    background: Color? = null,
    var maxLines: Int? = null,
    /** The space between lines */
    var tracking: Float = 4f,
    var horizontalAlignment: Alignment = StartAlignment,
    var verticalAlignment: Alignment = StartAlignment,
) : GenericContainer(id, parent, size, padding, background) {

    private val flexChildren = mutableListOf<Element>()
    override val computedChildren: List<Element>
        get() = flexChildren

    private fun getTotalChildWidthWithGaps(): Float = getTotalChildWidth()

    private fun calculateFlex(elements: List<Placeable>): FlexPlacementHelper {
        requireNotNull(placeable.width) { "The placeable has to have defined width to apply flex." }

        flexChildren.clear()
        val flexHelper = FlexPlacementHelper()
        val queue = ArrayDeque(elements)

        fun addFlexChild(element: Element) {
            element.measure(this)
            flexHelper.add(element.placeable)
            flexChildren.add(element)
        }

        while (queue.isNotEmpty()) {
            if (maxLines != null && flexHelper.getNumBuckets() > maxLines!!) {
                break
            }

            val placeable = queue.removeFirst()
            val currentBucketWidth = flexHelper.getCurrentBucketWidth()
            val childWidth = placeable.width ?: 0f

            val element = placeable.element
            if (element !is TextLineElement && currentBucketWidth + childWidth < getInnerWidth()) {
                addFlexChild(element)
                continue
            }

            // The element does not fit into the container, so we have to process it further
            if (element !is TextLineElement) {
                flexHelper.addBucket()
                addFlexChild(element)
                continue
            }

            val remainingSpace = getInnerWidth() - currentBucketWidth

            when (val result = element.applySplit(remainingSpace, getInnerWidth())) {
                is SplitResult.Failed -> {
                    // Can only fail if the string is empty, do nothing
                }
                is SplitResult.FitsCompletely -> {
                    addFlexChild(result.element)
                }
                is SplitResult.NewLine -> {
                    flexHelper.addBucket()
                    queue.addFirst(result.remainder.placeable)
                }
                is SplitResult.TakeNextLine -> {
                    flexHelper.addBucket()
                    addFlexChild(result.newLine)
                    flexHelper.addBucket()
                    queue.addFirst(result.remainder.placeable)
                }
                is SplitResult.Wrap -> {
                    addFlexChild(result.fittingPart)
                    flexHelper.addBucket()
                    queue.addFirst(result.remainder.placeable)
                }
            }
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
        val flexHeight = flex.getTotalHeight(tracking)

        var offsetX = 0f
        var offsetY = 0f

        val maxBucket = if (maxLines != null) {
            maxLines!!.coerceAtMost(flex.getNumBuckets())
        } else {
            flex.getNumBuckets()
        }

        (0 until maxBucket).forEach { bucketIndex ->
            val bucket = flex.getBucket(bucketIndex)
            val bucketWidth = flex.getBucketWidth(bucketIndex)
            val bucketMaxHeight = flex.getBucketMaxHeight(bucketIndex)
            bucket.forEach {
                it.x = padding.left + offsetX + horizontalAlignment.calculate(bucketWidth, availableWidth)
                it.y = padding.top + offsetY + verticalAlignment.calculate(flexHeight, availableHeight)
                offsetX += it.width!!
            }
            offsetX = 0f
            offsetY += bucketMaxHeight + tracking
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
                val flex = calculateFlex(children.asPlaceables())
                flex.getTotalHeight(tracking) + this.padding.vertical
            }
            else -> placeable.height
        }, container)
    }
}