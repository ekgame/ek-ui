package lt.ekgame.ui.elements

import com.github.ajalt.colormath.Color
import lt.ekgame.ui.Container
import lt.ekgame.ui.constraints.SizeConstraints

class RectangleElement(
    id: String = "",
    override val parent: Container?,
    var color: Color,
    size: SizeConstraints = SizeConstraints.DEFAULT,
) : AbstractElement(id, parent, size)