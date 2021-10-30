package lt.ekgame.ui.builder

import lt.ekgame.ui.*
import lt.ekgame.ui.constraints.*
import lt.ekgame.ui.containers.*
import lt.ekgame.ui.elements.RectangleElement
import lt.ekgame.ui.elements.TextLineElement
import lt.ekgame.ui.text.TextStyle
import java.awt.Color
import java.awt.event.TextEvent

class UiBuilder(private val container: Container) {
    fun addElement(factory: (parent: Container) -> Element): Element {
        val subElement = factory.invoke(container)
        container.addChild(subElement)
        return subElement
    }

    fun addContainer(
        builder: UiBuilder.() -> Unit,
        factory: (parent: Container) -> Container
    ): Container {
        val targetContainer = factory.invoke(container)
        val instance = UiBuilder(targetContainer)
        builder.invoke(instance)
        container.addChild(targetContainer)
        return targetContainer
    }

    fun add(builder: UiBuilder.() -> Unit) {
        builder.invoke(this)
    }
}

fun <T : Container>buildUi(container: T, builder: UiBuilder.() -> Unit): T {
    val instance = UiBuilder(container)
    builder.invoke(instance)
    return container
}

fun buildRootUi(builder: UiBuilder.() -> Unit): RootContainer {
    return buildUi(RootContainer(), builder)
}

fun UiBuilder.Row(
    width: Size = MaxAvailable,
    height: Size = MaxAvailable,
    minWidth: Size = width,
    minHeight: Size = height,
    maxWidth: Size = width,
    maxHeight: Size = height,
    background: Color? = null,
    padding: PaddingValues = PaddingValues.ZERO,
    horizontalAlignment: Alignment = StartAlignment,
    verticalAlignment: Alignment = StartAlignment,
    gap: Float = 0f,
    builder: UiBuilder.() -> Unit
) = addContainer(builder) {
    RowContainer(
        it,
        padding = padding,
        background = background,
        horizontalAlignment = horizontalAlignment,
        verticalAlignment = verticalAlignment,
        gap = gap,
        size = SizeConstraints(
            width = width,
            height = height,
            minWidth = minWidth,
            minHeight = minHeight,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
        ),
    )
}

fun UiBuilder.FlexRow(
    width: Size = MaxAvailable,
    height: Size = ContentSize,
    minWidth: Size = width,
    minHeight: Size = height,
    maxWidth: Size = width,
    maxHeight: Size = height,
    background: Color? = null,
    padding: PaddingValues = PaddingValues.ZERO,
    horizontalAlignment: Alignment = StartAlignment,
    verticalAlignment: Alignment = StartAlignment,
    verticalContainerAlignment: Alignment = StartAlignment,
    gap: Float = 0f,
    builder: UiBuilder.() -> Unit
) = addContainer(builder) {
    FlexRowContainer(
        it,
        padding = padding,
        background = background,
        horizontalAlignment = horizontalAlignment,
        verticalAlignment = verticalAlignment,
        verticalContainerAlignment = verticalContainerAlignment,
        gap = gap,
        size = SizeConstraints(
            width = width,
            height = height,
            minWidth = minWidth,
            minHeight = minHeight,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
        ),
    )
}

fun UiBuilder.FlexColumn(
    width: Size = ContentSize,
    height: Size = MaxAvailable,
    minWidth: Size = width,
    minHeight: Size = height,
    maxWidth: Size = width,
    maxHeight: Size = height,
    background: Color? = null,
    padding: PaddingValues = PaddingValues.ZERO,
    horizontalAlignment: Alignment = StartAlignment,
    horizontalContainerAlignment: Alignment = StartAlignment,
    verticalAlignment: Alignment = StartAlignment,
    gap: Float = 0f,
    builder: UiBuilder.() -> Unit
) = addContainer(builder) {
    FlexColumnContainer(
        it,
        padding = padding,
        background = background,
        horizontalAlignment = horizontalAlignment,
        horizontalContainerAlignment = horizontalContainerAlignment,
        verticalAlignment = verticalAlignment,
        gap = gap,
        size = SizeConstraints(
            width = width,
            height = height,
            minWidth = minWidth,
            minHeight = minHeight,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
        ),
    )
}

fun UiBuilder.Column(
    width: Size = MaxAvailable,
    height: Size = MaxAvailable,
    minWidth: Size = width,
    minHeight: Size = height,
    maxWidth: Size = width,
    maxHeight: Size = height,
    background: Color? = null,
    padding: PaddingValues = PaddingValues.ZERO,
    horizontalAlignment: Alignment = StartAlignment,
    verticalAlignment: Alignment = StartAlignment,
    gap: Float = 0f,
    builder: UiBuilder.() -> Unit
) = addContainer(builder) {
    ColumnContainer(
        it,
        padding = padding,
        background = background,
        horizontalAlignment = horizontalAlignment,
        verticalAlignment = verticalAlignment,
        gap = gap,
        size = SizeConstraints(
            width = width,
            height = height,
            minWidth = minWidth,
            minHeight = minHeight,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
        ),
    )
}

fun UiBuilder.Box(
    width: Size = MaxAvailable,
    height: Size = MaxAvailable,
    minWidth: Size = width,
    minHeight: Size = height,
    maxWidth: Size = width,
    maxHeight: Size = height,
    background: Color? = null,
    padding: PaddingValues = PaddingValues.ZERO,
    horizontalAlignment: Alignment = StartAlignment,
    verticalAlignment: Alignment = StartAlignment,
    builder: UiBuilder.() -> Unit
) = addContainer(builder) {
    BoxContainer(
        it,
        padding = padding,
        background = background,
        horizontalAlignment = horizontalAlignment,
        verticalAlignment = verticalAlignment,
        size = SizeConstraints(
            width = width,
            height = height,
            minWidth = minWidth,
            minHeight = minHeight,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
        ),
    )
}

fun UiBuilder.Rectangle(
    color: Color,
    width: Size = MaxAvailable,
    height: Size = MaxAvailable,
    minWidth: Size = width,
    minHeight: Size = height,
    maxWidth: Size = width,
    maxHeight: Size = height,
) = addElement {
    RectangleElement(
        it,
        color = color,
        size = SizeConstraints(
            width = width,
            height = height,
            minWidth = minWidth,
            minHeight = minHeight,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
        ),
    )
}

fun UiBuilder.Text(
    text: String,
    style: TextStyle,
    width: Size = ContentSize,
    height: Size = ContentSize,
    minWidth: Size = width,
    minHeight: Size = height,
    maxWidth: Size = width,
    maxHeight: Size = height,
) = addElement {
    TextLineElement(
        it,
        text = text,
        style = style,
        size = SizeConstraints(
            width = width,
            height = height,
            minWidth = minWidth,
            minHeight = minHeight,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
        ),
    )
}