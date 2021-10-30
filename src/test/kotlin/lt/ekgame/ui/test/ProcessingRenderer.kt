package lt.ekgame.ui.test

import lt.ekgame.ui.Element
import lt.ekgame.ui.containers.AbstractContainer
import lt.ekgame.ui.containers.GenericContainer
import lt.ekgame.ui.elements.RectangleElement
import lt.ekgame.ui.elements.TextLineElement
import lt.ekgame.ui.rendering.ElementRenderer
import processing.core.PApplet
import java.awt.Color

class ProcessingRenderer(val context: PApplet) : ElementRenderer {

    override fun render(element: Element) {
        when (element) {
            is GenericContainer -> render(element)
            is AbstractContainer -> render(element)
            is RectangleElement -> render(element)
            is TextLineElement -> render(element)
        }
    }

    fun PApplet.color(color: Color) = this.color(color.red, color.green, color.blue, color.alpha)

    fun render(element: AbstractContainer) {
        element.children.forEach(::render)
    }

    fun render(element: GenericContainer) {
        val placeable = element.placeable
        val x = placeable.x ?: return
        val y = placeable.y ?: return
        val width = placeable.width ?: return
        val height = placeable.height ?: return

        context.pushMatrix()
        context.translate(x, y)
        if (element.background != null) {
            context.fill(context.color(element.background!!))
            context.noStroke()
            context.rect(0f, 0f, width, height)
        }
        render(element as AbstractContainer)
        context.popMatrix()
    }

    fun render(element: RectangleElement) {
        val placeable = element.placeable
        val x = placeable.x ?: return
        val y = placeable.y ?: return
        val width = placeable.width ?: return
        val height = placeable.height ?: return

        context.fill(context.color(element.color), element.color.alpha.toFloat())
        context.noStroke()
        context.rect(x, y, width, height)
    }

    fun render(element: TextLineElement) {
        val placeable = element.placeable
        val posX = placeable.x ?: return
        val posY = placeable.y ?: return
        val style = element.style

        context.fill(context.color(style.color))
        val font = style.computedFont as? ProcessingFont ?: return
        context.textFont(font.nativeFont)
        context.textSize(style.size)
        context.text(element.text, posX, posY + context.textAscent())
    }
}