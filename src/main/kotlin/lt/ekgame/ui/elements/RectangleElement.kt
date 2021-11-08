package lt.ekgame.ui.elements

import lt.ekgame.ui.Container
import lt.ekgame.ui.Element
import lt.ekgame.ui.constraints.SizeConstraints
import java.awt.Color

class RectangleElement(
    id: String = "",
    override val parent: Container?,
    var color: Color,
    size: SizeConstraints = SizeConstraints.DEFAULT,
) : AbstractElement(id, parent, size)