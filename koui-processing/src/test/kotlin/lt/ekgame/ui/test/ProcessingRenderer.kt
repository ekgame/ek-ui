package lt.ekgame.ui.test

import com.github.ajalt.colormath.Color
import lt.ekgame.ui.Element
import lt.ekgame.ui.containers.AbstractContainer
import lt.ekgame.ui.containers.GenericContainer
import lt.ekgame.ui.elements.RectangleElement
import lt.ekgame.ui.elements.TextLineElement
import lt.ekgame.ui.rendering.ElementRenderer
import processing.core.PApplet

class ProcessingRenderer(val context: PApplet) : ElementRenderer {

    override fun render(element: Element) {
        when (element) {
            is GenericContainer -> render(element)
            is AbstractContainer -> render(element)
            is RectangleElement -> render(element)
            is TextLineElement -> render(element)
        }
    }

    fun render(element: AbstractContainer) {
        element.computedChildren.forEach(::render)
    }

    fun render(element: GenericContainer) {
        val placeable = element.placeable
        if (!placeable.isValid) {
            return
        }
        val width = placeable.width ?: return
        val height = placeable.height ?: return

        val translation = element.transformMatrix.translation
        val scale = element.transformMatrix.scale
        val rotation = element.transformMatrix.rotation

        context.pushMatrix()
        context.translate(translation.x, translation.y)
        context.scale(scale.x, scale.y)
        context.rotate(rotation.x)
        if (element.background != null) {
            context.fill(element.background!!)
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

        context.fill(element.color)
        context.noStroke()
        context.rect(x, y, width, height)
    }

    fun render(element: TextLineElement) {
        val placeable = element.placeable
        val posX = placeable.x ?: return
        val posY = placeable.y ?: return
        val style = element.style

        context.fill(style.color)
        val font = style.computedFont as? ProcessingFont ?: return
        context.textFont(font.nativeFont)
        context.textSize(style.size)
        context.text(element.text, posX, posY + context.textAscent())

        if (style.underline) {
            val width = font.measureWidth(element.text, style)
            val yPos =  posY + context.textAscent() + 3
            context.stroke(style.color)
            context.line(posX, yPos, posX + width, yPos)
        }
    }

    fun PApplet.fill(color: Color) {
        val rgb = color.toSRGB()
        fill(rgb.redInt.toFloat(), rgb.greenInt.toFloat(), rgb.blueInt.toFloat(), color.alpha*255)
    }

    fun PApplet.stroke(color: Color) {
        val rgb = color.toSRGB()
        stroke(rgb.r*255, rgb.g*255, rgb.b*255, color.alpha)
    }
}
