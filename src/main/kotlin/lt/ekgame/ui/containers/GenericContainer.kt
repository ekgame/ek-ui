package lt.ekgame.ui.containers

import lt.ekgame.ui.Container
import lt.ekgame.ui.Element
import lt.ekgame.ui.constraints.*
import java.awt.Color

open class GenericContainer(
    id: String = "",
    parent: Container?,
    size: SizeConstraints = SizeConstraints.DEFAULT,
    padding: PaddingValues = PaddingValues.ZERO,
    var background: Color? = null,
) : AbstractContainer(id, parent, size, padding)