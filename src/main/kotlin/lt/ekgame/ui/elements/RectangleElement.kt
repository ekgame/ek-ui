package lt.ekgame.ui.elements

import lt.ekgame.ui.Element
import lt.ekgame.ui.constraints.SizeConstraints
import java.awt.Color

class RectangleElement(
    id: String = "",
    override val parent: Element?,
    val color: Color,
    override val size: SizeConstraints = SizeConstraints.DEFAULT,
) : AbstractElement(id, parent, size)