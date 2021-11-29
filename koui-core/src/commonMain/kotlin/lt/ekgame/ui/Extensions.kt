package lt.ekgame.ui

fun List<Element>.asPlaceables() = this.map { it.placeable }

fun Element.dump(depth: Int = 0): String {
    if (this is Container) {
        return dumpContainer(depth)
    }
    return toString().indented(depth)
}

fun Container.dumpContainer(depth: Int = 0): String {
    if (depth > 10) {
        return toString().indented(depth)
    }
    var buffer = "${toString()} {".indented(depth)
    buffer += "\n"
    buffer += children.joinToString("\n") { it.dump(depth + 2) }
    buffer += "\n"
    buffer += "}".indented(depth)
    return buffer
}

fun String.indented(size: Int): String = lines().joinToString("\n") { " ".repeat(size) + it }