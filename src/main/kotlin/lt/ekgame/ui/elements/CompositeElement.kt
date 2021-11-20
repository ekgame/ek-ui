package lt.ekgame.ui.elements

import dev.romainguy.kotlin.math.Mat4
import lt.ekgame.ui.Container
import lt.ekgame.ui.Placeable
import lt.ekgame.ui.builder.UiBuilder
import lt.ekgame.ui.constraints.SizeConstraints
import lt.ekgame.ui.containers.AbstractContainer
import lt.ekgame.ui.events.Event
import lt.ekgame.ui.events.EventListener
import kotlin.reflect.KClass

abstract class CompositeElement(
    parent: Container,
    size: SizeConstraints,
    id: String = "",
) : AbstractContainer(id, parent, size) {

    lateinit var proxyContainer: Container

    override val transformMatrix: Mat4 = Mat4.identity()

    override val placeable: Placeable
        get() = proxyContainer.placeable

    abstract fun initComposition(): Container

    open fun initListeners() {}

    fun initComposite() {
        proxyContainer = initComposition()
        initListeners()
    }

    override fun <T : Event> listen(clazz: KClass<T>, listener: EventListener<Event>) {
        if (this::proxyContainer.isInitialized) {
            proxyContainer.listen(clazz, listener)
        }
    }

    override fun measure(container: Container?): Boolean {
        return proxyContainer.measure(container)
    }
}

fun <T : Container> CompositeElement.composeElement(builder: UiBuilder.() -> T): T {
    val instance = UiBuilder(this)
    return builder.invoke(instance)
}