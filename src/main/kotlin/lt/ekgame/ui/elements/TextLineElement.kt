package lt.ekgame.ui.elements

import lt.ekgame.ui.Container
import lt.ekgame.ui.Element
import lt.ekgame.ui.constraints.ContentSize
import lt.ekgame.ui.constraints.Size
import lt.ekgame.ui.constraints.SizeConstraints
import lt.ekgame.ui.text.TextStyle

class TextLineElement(
    override val parent: Element?,
    val text: String,
    val style: TextStyle,
    override val size: SizeConstraints = SizeConstraints.DEFAULT,
) : AbstractElement(parent, size) {

    override fun computeWidth(container: Container?, constraint: Size): Float? {
        return if (constraint is ContentSize) {
            style.computedFont.measureWidth(text, style)
        } else {
            super.computeWidth(container, constraint)
        }
    }

    override fun computeHeight(container: Container?, constraint: Size): Float? {
        return if (constraint is ContentSize) {
            style.computedFont.measureHeight(text, style)
        } else {
            super.computeHeight(container, constraint)
        }
    }
}