package lt.ekgame.ui.elements

import lt.ekgame.ui.Container
import lt.ekgame.ui.Element
import lt.ekgame.ui.constraints.Size
import lt.ekgame.ui.constraints.SizeConstraints
import lt.ekgame.ui.text.*

class TextLineElement(
    id: String = "",
    parent: Element?,
    val text: String,
    val style: TextStyle,
    var proxyFor: TextLineElement? = null,
) : AbstractElement(id, parent, SizeConstraints.CONTENT) {

    val tokenized by lazy { TextTokenizer(text).tokenize() }

    override fun computeWidth(container: Container?, constraint: Size): Float {
        return style.computedFont.measureWidth(text, style)
    }

    override fun computeHeight(container: Container?, constraint: Size): Float {
        return style.computedFont.measureHeight(text, style)
    }

    private fun measureString(string: String): Float = style.computedFont.measureWidth(string, style)

    fun splitToWidth(width: Float, allowBreakingIndividualWords: Boolean = false): Pair<TextLineElement?, TextLineElement?> {
        fun createPair(head: String?, tail: String): Pair<TextLineElement?, TextLineElement?> {
            val headElement = if (head === null) {
                null
            } else {
                TextLineElement(id, parent, head.trimEnd(), style, proxyFor ?: this)
            }
            val tailElement = TextLineElement(id, parent, tail.trimStart(), style, proxyFor ?: this)
            return headElement to tailElement
        }

        val tokens = ArrayDeque(tokenized)
        if (tokens.size == 0 || tokens.size == 1 && !allowBreakingIndividualWords) {
            // Can't split the element any further
            return this to null
        }

        val firstToken = tokens.first()
        if (firstToken is NewLineToken) {
            return createPair("", tokens.join())
        }

        if (firstToken is WordToken && allowBreakingIndividualWords && measureString(firstToken.word) > width) {
            tokens.removeFirst()
            val (wordHead, wordTail) = splitWord(width, firstToken.word)
            return if (wordTail != null) {
                createPair(wordHead, wordTail + tokens.join())
            } else {
                createPair(wordHead, tokens.join())
            }
        }

        val fittingTokens = mutableListOf<TextToken>()
        while (tokens.isNotEmpty()) {
            val currentToken = tokens.removeFirst()
            if (currentToken is NewLineToken) {
                return createPair(fittingTokens.join(), tokens.join().trimStart())
            }

            val currentText = (fittingTokens + currentToken).join()
            val currentWidth = measureString(currentText)

            if (currentWidth > width) {
                if (fittingTokens.isEmpty()) {
                    // Can't fit any elements to the supplied width
                    return this to null
                }
                if (currentToken !is SeparatorToken) {
                    tokens.addFirst(currentToken)
                }
                return createPair(fittingTokens.join(), tokens.join())
            } else {
                fittingTokens.add(currentToken)
            }
        }

        // Failed to split
        return this to null
    }

    fun splitWord(width: Float, word: String): Pair<String, String?> {
        var size = 1
        while (size < word.length && measureString(word.substring(0, size)) < width) {
            size++
        }

        val wordSize = (size - 1).coerceAtLeast(1)
        val head = word.substring(0, wordSize)
        if (wordSize == word.length) {
            return head to null
        }

        return head to word.substring(size - 1)
    }
}