package lt.ekgame.ui.test

import lt.ekgame.ui.builder.*
import lt.ekgame.ui.constraints.*
import lt.ekgame.ui.containers.RootContainer
import lt.ekgame.ui.text.Font
import lt.ekgame.ui.text.TextStyle
import processing.core.PApplet
import java.awt.Color
import kotlin.math.roundToInt
import kotlin.random.Random

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
            background = Color(0x6C6C6C),
        ) {
            Box(
                maxWidth = AbsoluteSize(500f),
                background = Color(0x888888),
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
            background = Color(0x2C2C2C),
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
            background = Color(0x333333),
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
            background = Color(0xC4C4C4),
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
                color = Color(0xC4C4C4),
                width = AbsoluteSize(80f),
                height = AbsoluteSize(80f),
            )
            Column(
                id = "article",
                height = ContentSize,
                background = Color(0x000000)
            ) {
                TextContainer(padding = PaddingValues.vertical(4f, 6f)) {
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
            background = Color(0xC4C4C4),
            height = AbsoluteSize(80f),
            width = AbsoluteSize(80f),
        ) {
            Rectangle(
                color = Color(0x88000000.toInt(), true),
                height = AbsoluteSize(30f)
            )
        }
    }

    /**
     * Build a UI only using builders.
     */
    fun buildTestUi2() = buildRootUi {
        Box(
            background = Color.white,
            padding = PaddingValues.of(10f),
            height = ContentSize,
        ) {
            TextContainer(
                verticalAlignment = EndAlignment
            ) {
                Text("sdf sdf sdfd!\n", style = TextStyle(font, bold = true, underline = false, size = 32f))
                Text("sdf sdf sdfd sfsd fsdf!\n", style = TextStyle(font, bold = true, underline = true, size = 32f))
                Text("sdf sdf sdfd sfsd fsdf ds fsd fds fsdf!\n", style = TextStyle(font, bold = true, underline = false, size = 32f))
                Text("sdf sdf sdfd sfsd fsdf ds fsd fds fsdf sdf sdf sdfd!\n", style = TextStyle(font, bold = true, underline = true, size = 32f))
                Text("sdf sdf sdfd sfsd fsdf ds fsd fds fsdf sdf sdf sdfd sfsd fsdf!\n", style = TextStyle(font, bold = true, underline = false, size = 32f))
                Text("sdf sdf sdfd sfsd fsdf ds fsd fds fsdf sdf sdf sdfd sfsd fsdf ds fsd fds fsdf!\n", style = TextStyle(font, bold = true, underline = true, size = 32f))
            }
        }
    }

    val renderer = ProcessingRenderer(this)
    lateinit var font: Font

    override fun setup() {
        super.setup()
        font = buildFont(this, "Arial")
        surface.setResizable(true)
        root = buildTestUi()
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

fun main(args: Array<String>) {
    UiTest.run(args)
}


