package lt.ekgame.ui.text

class TextTokenizer(val text: String) {
    var cursor = 0
    val results = mutableListOf<TextToken>()

    fun tokenize(): List<TextToken> {
        if (results.isNotEmpty()) {
            return results
        }

        while (!isFinished()) {
            if (scanSeparator()) continue
            if (scanNewLine()) continue
            scanWord()
        }

        return results
    }

    fun isFinished() = cursor >= text.length

    fun peek(offset: Int = 0): Char? = text.getOrNull(cursor + offset)

    fun next() = text[cursor++]

    fun isSeparatorCharacter(char: Char) = char == ' '

    fun isNewLineChar(char: Char) = char == '\n' || char == '\r'

    fun scanSeparator(): Boolean {
        if (!isSeparatorCharacter(peek()!!)) {
            return false
        }

        var separator = ""
        while (!isFinished() && isSeparatorCharacter(peek()!!)) {
            separator += next()
        }

        results.add(SeparatorToken(separator))
        return true
    }

    fun scanNewLine(): Boolean {
        if (peek() == '\r') {
            next()
            if (peek() == '\n') {
                next()
            }
            results.add(NewLineToken)
            return true
        }

        if (peek() == '\n') {
            next()
            results.add(NewLineToken)
            return true
        }

        return false
    }

    fun scanWord(): Boolean {
        var word = ""

        while (!isFinished()) {
            val current = peek()
            if (current == null || isNewLineChar(current) || isSeparatorCharacter(current)) {
                break
            }
            word += next()
        }

        results.add(WordToken(word))
        return true
    }
}

sealed class TextToken {
    val isWhitespace: Boolean
        get() = this is SeparatorToken || this is NewLineToken
}
class WordToken(val word: String) : TextToken()
class SeparatorToken(val separator: String) : TextToken()
object NewLineToken : TextToken()

fun Collection<TextToken>.join() = this.map {
        when (it) {
            NewLineToken -> "\n"
            is SeparatorToken -> it.separator
            is WordToken -> it.word
        }
    }.joinToString("")

fun Collection<TextToken>.trimEnd(): Collection<TextToken> {
    val list = ArrayDeque(this)

    while (true) {
        val last = list.lastOrNull() ?: break
        if (last.isWhitespace) {
            list.removeLast()
        } else {
            break
        }
    }

    return list
}