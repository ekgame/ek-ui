package lt.ekgame.ui.test

import com.github.ajalt.colormath.Color
import lt.ekgame.ui.Container
import lt.ekgame.ui.Debug
import lt.ekgame.ui.builder.*
import lt.ekgame.ui.constraints.*
import lt.ekgame.ui.containers.BoxContainer
import lt.ekgame.ui.containers.RootContainer
import lt.ekgame.ui.dump
import lt.ekgame.ui.elements.CompositeElement
import lt.ekgame.ui.elements.TextLineElement
import lt.ekgame.ui.elements.composeElement
import lt.ekgame.ui.events.*
import lt.ekgame.ui.listen
import lt.ekgame.ui.text.Font
import lt.ekgame.ui.text.TextStyle
import lt.ekgame.ui.units.Point
import processing.core.PApplet
import processing.core.PConstants
import processing.event.KeyEvent
import kotlin.math.roundToInt
import com.github.ajalt.colormath.model.RGB;

class UiTest : PApplet() {

    lateinit var root: RootContainer

    val commonPadding = 8f
/*
    /**
     * Build UI without using any builders, just manually building UI graph.
     */
    fun buildUiManually(): RootContainer {
        return RootContainer(width.toFloat(), height.toFloat()).apply {
            addChild(GenericContainer(this, RowLayout(), background = Color.CYAN).apply {
                addChild(RectangleElement(this, Color.RED, maxWidthConstraint = FixedSizeConstraint(500f), minWidthConstraint = FixedSizeConstraint(300f)))
                addChild(RectangleElement(this, Color.GREEN, maxWidthConstraint = FixedSizeConstraint(500f), minWidthConstraint = FixedSizeConstraint(300f)))
                addChild(RectangleElement(this, Color.BLUE, maxWidthConstraint = FixedSizeConstraint(500f), minWidthConstraint = FixedSizeConstraint(300f)))
            })
        }
    }
*/

    /**
     * Build a UI only using builders.
     */
    fun buildTestUi() = buildRootUi {
        Container {
            Column {
                Menu {
                    MenuItem("Home")
                    MenuItem("Pricing")
                    MenuItem("About")
                    MenuItem("Contact us")
                }
                Row {
                    Content {
                        Article(
                            title = "Vae, ferox acipenser!",
                            description = "Per guest prepare one cup of honey with toasted mackerel for dessert.",
                        )
                        Article(
                            title = "Agripetas sunt resistentias de raptus cursus.",
                            description = "Mackerel tastes best with condensed milk and lots of mustard.",
                        )
                        Article(
                            title = "Cur clabulare prarere?",
                            description = "Chocolate stew has to have a raw, chilled tofu component.",
                        )
                    }
                    Sidebar {
                        SidebarItem()
                        SidebarItem()
                        SidebarItem()
                        SidebarItem()
                    }
                }
            }
        }
    }

    fun UiBuilder.Container(builder: UiBuilder.() -> Unit) = add {
        Box(
            horizontalAlignment = CenterAlignment,
            background = RGB("#6C6C6C"),
        ) {
            Box(
                maxWidth = AbsoluteSize(500f),
                background = RGB("#888888"),
                builder = builder,
            )
        }
    }

    fun UiBuilder.Content(builder: UiBuilder.() -> Unit) = add {
        Box(
            padding = PaddingValues.of(commonPadding),
        ) {
            Column(
                gap = commonPadding,
                builder = builder,
            )
        }
    }

    fun UiBuilder.Sidebar(builder: UiBuilder.() -> Unit) = add {
        Box(
            padding = PaddingValues.of(commonPadding),
            background = RGB("#2C2C2C"),
            width = ContentSize,
        ) {
            Column(
                gap = commonPadding,
                builder = builder,
                width = ContentSize,
            )
        }
    }

    fun UiBuilder.Menu(builder: UiBuilder.() -> Unit) = add {
        Box(
            padding = PaddingValues.of(commonPadding),
            background = RGB("#333333"),
            height = ContentSize,
        ) {
            FlexRow(
                gap = commonPadding,
                builder = builder,
            )
        }
    }

    fun UiBuilder.MenuItem(name: String) = add {
        Box(
            background = RGB("#C4C4C4"),
            width = ContentSize,
            height = ContentSize,
            padding = PaddingValues.of(commonPadding, commonPadding*2)
        ) {
            Text(
                text = name,
                style = TextStyle(font)
            )
        }
    }

    fun UiBuilder.Article(
        title: String,
        description: String,
    ) = add {
        Row(
            gap = commonPadding,
            height = ContentSize,
        ) {
            Rectangle(
                color = RGB("#C4C4C4"),
                width = AbsoluteSize(80f),
                height = AbsoluteSize(80f),
            )
            Column(
                id = "article",
                height = ContentSize,
            ) {
                TextContainer(
                    padding = PaddingValues.vertical(4f, 6f)
                ) {
                    Text(title, style = TextStyle(font, size = 16f))
                }
                TextContainer {
                    Text(description, style = TextStyle(font))
                }
            }
        }
    }

    fun UiBuilder.SidebarItem() = add {
        Box(
            verticalAlignment = EndAlignment,
            background = RGB("#C4C4C4"),
            height = AbsoluteSize(80f),
            width = AbsoluteSize(80f),
        ) {
            Rectangle(
                color = RGB("#88000000"),
                height = AbsoluteSize(30f),
            )
        }
    }

    fun UiBuilder.Button(
        text: String,
        id: String = "",
        onClick: (MouseClickedEvent) -> Unit = {}
    ) = addElement {
        ButtonElement(id, it, text, font, onClick)
    }

    /**
     * Build a UI only using builders.
     */
    fun buildTestUi2() = buildRootUi {
        Box(
            background = RGB("#FFFFFF"),
            padding = PaddingValues.of(10f),
            maxHeight = ContentSize,
        ) {
            TextContainer(
                maxLines = 5,
            ) {
                Text("Contencios peregrinatione! The rumour is a photonic starship. Nutrix volares, tanquam castus ausus. ", TextStyle(font))
                Text("Contencios peregrinatione! The rumour is a photonic starship. Nutrix volares, tanquam castus ausus. ", TextStyle(font))
                Text("Contencios peregrinatione! The rumour is a photonic starship. Nutrix volares, tanquam castus ausus.", TextStyle(font))
            }
        }
    }

    val renderer = ProcessingRenderer(this)
    lateinit var font: Font

    override fun setup() {
        super.setup()
        font = buildFont(this, "Arial")
        surface.setResizable(true)
        root = buildTestUi2()
        println(root.dump())
    }

    override fun mouseMoved(event: processing.event.MouseEvent) {
        root.propagateEvent(InternalMouseMoveEvent(Point(event.x.toFloat(), event.y.toFloat())))
    }

    override fun mouseClicked(event: processing.event.MouseEvent) {
        val button = when (event.button) {
            PConstants.LEFT -> MouseButton.LEFT
            PConstants.RIGHT -> MouseButton.RIGHT
            PConstants.CENTER -> MouseButton.MIDDLE
            else -> error("Invalid mouse button: ${event.button}")
        }
        root.propagateEvent(InternalMouseClickedEvent(Point(event.x.toFloat(), event.y.toFloat()), button = button))
    }

    override fun keyPressed(event: KeyEvent) {
        if (event.key == ' ') {
            Debug.enabled = true
        }
    }

    override fun keyReleased(event: KeyEvent) {
        if (event.key == ' ') {
            Debug.enabled = false
        }
    }

    override fun draw() {
        background(0x000000)
        root.width = width.toFloat()
        root.height = height.toFloat()
        root.measure(null)
        renderer.render(root)
        strokedText("FPS: ${frameRate.roundToInt()} [${width}x$height]", 10f, height - 10f)
    }

    fun strokedText(string: String, x: Float, y: Float) {
        fill(0)
        text(string, x + 1, y + 1)
        text(string, x - 1, y - 1)
        text(string, x + 1, y - 1)
        text(string, x - 1, y + 1)
        fill(255)
        text(string, x, y)
    }

    companion object {
        fun run(args: Array<String>) = UiTest().apply {
            setSize(856, 482)
            runSketch(args)
        }
    }
}

class ButtonElement(
    id: String,
    parent: Container,
    val text: String,
    val font: Font,
    val onClick: (MouseClickedEvent) -> Unit = {}
) : CompositeElement(parent, SizeConstraints.CONTENT, id) {

    lateinit var container: BoxContainer
    lateinit var textElement: TextLineElement

    override fun initComposition() = composeElement {
        container = Box(
            id = id,
            background = RGB("#000000"),
            padding = PaddingValues.of(10f, 20f),
            width = ContentSize,
            height = ContentSize,
        ) {
            textElement = Text(id.ifEmpty { text }, TextStyle(font, color = RGB("#FFFFFF")))
        }

        container
    }

    override fun initListeners() {
        listen<PointerEnteredEvent> {
            container.background = RGB("#888888")
        }

        listen<PointerLeftEvent> {
            container.background = RGB("#000000")
        }

        textElement.listen<PointerEnteredEvent> {
            textElement.style.color = RGB("#FF0000")
        }

        textElement.listen<PointerLeftEvent> {
            textElement.style.color = RGB("#FFFFFF")
        }

        listen(onClick)
    }
}

fun main(args: Array<String>) {
    UiTest.run(args)
}


