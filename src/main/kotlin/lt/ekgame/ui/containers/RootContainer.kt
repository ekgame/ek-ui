package lt.ekgame.ui.containers

import lt.ekgame.ui.Element
import lt.ekgame.ui.Placeable
import lt.ekgame.ui.constraints.AbsoluteSize
import lt.ekgame.ui.constraints.Size
import lt.ekgame.ui.constraints.SizeConstraints
import processing.core.PApplet

class RootContainer(
    var width: Float = 0f,
    var height: Float = 0f,
) : BoxContainer("root", null) {

    override val placeable = object : Placeable {
        override val element: Element
            get() = this@RootContainer

        override var x: Float? = 0f
            set(value) = error("Unsupported operation setX for RootContainer")

        override var y: Float? = 0f
            set(value) = error("Unsupported operation setY for RootContainer")

        override var width: Float?
            get() = this@RootContainer.width
            set(value) = error("Unsupported operation setWidth for RootContainer")

        override var height: Float?
            get() = this@RootContainer.height
            set(value) = error("Unsupported operation setHeight for RootContainer")
    }

    override var size: SizeConstraints = object : SizeConstraints() {
            override val width: Size
                get() = AbsoluteSize(this@RootContainer.width)

            override val height: Size
                get() = AbsoluteSize(this@RootContainer.height)

            override val minWidth: Size
                get() = width

            override val minHeight: Size
                get() = height

            override val maxWidth: Size
                get() = width

            override val maxHeight: Size
                get() = height
        }
        set(value) {}
}
