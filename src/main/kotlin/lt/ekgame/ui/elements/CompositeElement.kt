package lt.ekgame.ui.elements

import lt.ekgame.ui.Container
import lt.ekgame.ui.Placeable
import lt.ekgame.ui.builder.UiBuilder
import lt.ekgame.ui.constraints.SizeConstraints
import lt.ekgame.ui.containers.AbstractContainer

abstract class CompositeElement(
    parent: Container,
    size: SizeConstraints,
    id: String = "",
) : AbstractContainer(id, parent, size) {

    lateinit var proxyContainer: Container

    override val placeable: Placeable
        get() = proxyContainer.placeable

    abstract fun load(): Container

    fun initComposite() {
        proxyContainer = load()
    }

    override fun measure(container: Container?): Boolean {
        return proxyContainer.measure(container)
    }
}

fun <T : Container> CompositeElement.composeElement(builder: UiBuilder.() -> T): T {
    val instance = UiBuilder(this)
    return builder.invoke(instance)
}