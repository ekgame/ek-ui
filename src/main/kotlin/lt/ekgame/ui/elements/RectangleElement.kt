package lt.ekgame.ui.elements

import lt.ekgame.ui.Container
import lt.ekgame.ui.Element
import lt.ekgame.ui.constraints.SizeConstraints
import java.awt.Color

class RectangleElement(
    id: String = "",
    override val parent: Container?,
    val color: Color,
    override val size: SizeConstraints = SizeConstraints.DEFAULT,
) : AbstractElement(id, parent, size)