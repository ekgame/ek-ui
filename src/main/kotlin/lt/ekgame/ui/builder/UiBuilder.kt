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
    id: String = "",
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
        id,
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
    id: String = "",
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
        id,
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
    id: String = "",
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
        id,
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
    id: String = "",
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
        id,
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
    id: String = "",
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
        id,
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
    id: String = "",
    color: Color,
    width: Size = MaxAvailable,
    height: Size = MaxAvailable,
    minWidth: Size = width,
    minHeight: Size = height,
    maxWidth: Size = width,
    maxHeight: Size = height,
) = addElement {
    RectangleElement(
        id,
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
    id: String = "",
) = addElement {
    TextLineElement(
        id,
        it,
        text = text,
        style = style,
    )
}

fun UiBuilder.TextContainer(
    id: String = "",
    background: Color? = null,
    padding: PaddingValues = PaddingValues.ZERO,
    horizontalAlignment: Alignment = StartAlignment,
    verticalAlignment: Alignment = StartAlignment,
    tracking: Float = 0f,
    width: Size = MaxAvailable,
    height: Size = ContentSize,
    minWidth: Size = width,
    minHeight: Size = height,
    maxWidth: Size = width,
    maxHeight: Size = height,
    builder: UiBuilder.() -> Unit
) = addContainer(builder) {
    TextContainer(
        id,
        it,
        background = background,
        padding = padding,
        horizontalAlignment = horizontalAlignment,
        verticalAlignment = verticalAlignment,
        tracking = tracking,
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