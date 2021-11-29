package lt.ekgame.ui.containers

import com.github.ajalt.colormath.Color
import lt.ekgame.ui.Container
import lt.ekgame.ui.constraints.*

open class BoxContainer(
    id: String = "",
    parent: Container?,
    size: SizeConstraints = SizeConstraints.DEFAULT,
    padding: PaddingValues = PaddingValues.ZERO,
    background: Color? = null,
    var verticalAlignment: Alignment = StartAlignment,
    var horizontalAlignment: Alignment = StartAlignment,
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