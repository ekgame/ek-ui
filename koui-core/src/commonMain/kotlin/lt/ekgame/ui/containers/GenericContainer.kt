package lt.ekgame.ui.containers

import com.github.ajalt.colormath.Color
import lt.ekgame.ui.Container
import lt.ekgame.ui.constraints.*

open class GenericContainer(
    id: String = "",
    parent: Container?,
    size: SizeConstraints = SizeConstraints.DEFAULT,
    padding: PaddingValues = PaddingValues.ZERO,
    var background: Color? = null,
) : AbstractContainer(id, parent, size, padding)