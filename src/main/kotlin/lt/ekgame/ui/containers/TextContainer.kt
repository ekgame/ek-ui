package lt.ekgame.ui.containers

import lt.ekgame.ui.Container
import lt.ekgame.ui.Element
import lt.ekgame.ui.Placeable
import lt.ekgame.ui.asPlaceables
import lt.ekgame.ui.constraints.*
import lt.ekgame.ui.containers.helpers.FlexPlacementHelper
import lt.ekgame.ui.elements.TextLineElement
import java.awt.Color

class TextContainer(
    parent: Element?,
    size: SizeConstraints = SizeConstraints.CONTENT,
    padding: PaddingValues = PaddingValues.ZERO,
    background: Color? = null,
    val justified: Boolean = false,
    /** The space between lines */
    val tracking: Float = 4f,
    val horizontalAlignment: Alignment = StartAlignment,
    val verticalAlignment: Alignment = StartAlignment,
) : GenericContainer(parent, size, padding, background) {

    private val flexChildren = mutableListOf<Element>()
    override val computedChildren: List<Element>
        get() = flexChildren

    private fun getTotalChildWidthWithGaps(): Float = getTotalChildWidth()

    private fun calculateFlex(elements: List<Placeable>): FlexPlacementHelper {
        requireNotNull(placeable.width) { "The placeable has to have defined width to apply flex." }

        flexChildren.clear()
        val flexHelper = FlexPlacementHelper()
        val queue = ArrayDeque(elements)

        while (queue.isNotEmpty()) {
            val placeable = queue.removeFirst()
            val currentBucketWidth = flexHelper.getCurrentBucketWidth()
            val childWidth = placeable.width ?: 0f

            val element = placeable.element
            if (element !is TextLineElement && currentBucketWidth + childWidth < getInnerWidth()) {
                flexHelper.add(placeable)
                flexChildren.add(placeable.element)
                continue
            }

            // The element does not fit into the container, so we have to process it further
            if (element !is TextLineElement) {
                flexHelper.addBucket()
                flexHelper.add(placeable)
                flexChildren.add(placeable.element)
                continue
            }

            val remainingSpace = getInnerWidth() - currentBucketWidth
            val (head, tail) = element.splitToWidth(remainingSpace)
            if (head == null) {
                flexHelper.addBucket()
                if (tail != null) {
                    queue.addFirst(tail.placeable)
                }
                continue
            }

            head.measure(this)
            if (tail == null) {
                val (head2, tail2) = element.splitToWidth(getInnerWidth(), true)

                if (head2 == null) {
                    flexHelper.addBucket()
                    if (tail2 != null) {
                        queue.addFirst(tail2.placeable)
                    }
                } else if (tail2 == null) {
                    head2.measure(this)
                    flexHelper.addBucket()
                    flexHelper.add(head2.placeable)
                    flexChildren.add(head2)
                } else {
                    head2.measure(this)
                    flexHelper.addBucket()
                    flexHelper.add(head2.placeable)
                    flexChildren.add(head2)
                    flexHelper.addBucket()
                    tail2.measure(this)
                    queue.addFirst(tail2.placeable)
                }
            } else {
                flexHelper.add(head.placeable)
                flexChildren.add(head)
                flexHelper.addBucket()
                tail.measure(this)
                queue.addFirst(tail.placeable)
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

        (0 until flex.getNumBuckets()).forEach { bucketIndex ->
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