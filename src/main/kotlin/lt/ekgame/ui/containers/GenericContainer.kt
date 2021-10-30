package lt.ekgame.ui.containers

import lt.ekgame.ui.Element
import lt.ekgame.ui.constraints.*
import java.awt.Color

open class GenericContainer(
    parent: Element?,
    size: SizeConstraints = SizeConstraints.DEFAULT,
    padding: PaddingValues = PaddingValues.ZERO,
    val background: Color? = null,
) : AbstractContainer(parent, size, padding)