package lt.ekgame.ui.containers

import lt.ekgame.ui.Element
import lt.ekgame.ui.constraints.Alignment
import lt.ekgame.ui.constraints.PaddingValues
import lt.ekgame.ui.constraints.SizeConstraints
import lt.ekgame.ui.constraints.StartAlignment
import java.awt.Color

class TextContainer(
    parent: Element?,
    size: SizeConstraints = SizeConstraints.CONTENT,
    padding: PaddingValues = PaddingValues.ZERO,
    background: Color? = null,
    val horizontalAlignment: Alignment = StartAlignment,
) : GenericContainer(parent, size, padding, background) {

}