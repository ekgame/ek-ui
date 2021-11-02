package lt.ekgame.ui.containers

import lt.ekgame.ui.Container
import lt.ekgame.ui.Element
import lt.ekgame.ui.Placeable
import lt.ekgame.ui.constraints.*
import java.awt.Color

open class BoxContainer(
    id: String = "",
    parent: Element?,
    size: SizeConstraints = SizeConstraints.DEFAULT,
    padding: PaddingValues = PaddingValues.ZERO,
    background: Color? = null,
    val verticalAlignment: Alignment = StartAlignment,
    val horizontalAlignment: Alignment = StartAlignment,
) : GenericContainer(id, parent, size, padding, background) {

    override fun measure(container: Container?): Boolean {
        if (!super.measure(container) || !isValidPlaceable()) {
            return false
        }

        val remeasuredChildren = getRemeasuredChildren(container)
        val availableWidth = placeable.width!! - this.padding.horizontal
        val availableHeight = placeable.height!! - this.padding.vertical

        remeasuredChildren.forEach {
            it.x = padding.left + horizontalAlignment.calculate(it.width!!, availableWidth)
            it.y = padding.top + verticalAlignment.calculate(it.height!!, availableHeight)
        }

        return true
    }
}