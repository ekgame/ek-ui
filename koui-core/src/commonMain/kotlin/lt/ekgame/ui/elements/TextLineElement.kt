package lt.ekgame.ui.elements

import lt.ekgame.ui.Container
import lt.ekgame.ui.Element
import lt.ekgame.ui.constraints.Size
import lt.ekgame.ui.constraints.SizeConstraints
import lt.ekgame.ui.text.*

class TextLineElement(
    id: String = "",
    parent: Container?,
    var text: String,
    var style: TextStyle,
    val proxyFor: TextLineElement? = null,
) : AbstractElement(id, parent, SizeConstraints.CONTENT), WrappingElement<TextLineElement> {

    val tokenized by lazy {
        TextTokenizer(text).tokenize()
    }

    override fun computeWidth(container: Container?, constraint: Size): Float {
        return style.computedFont.measureWidth(text, style)
    }

    override fun computeHeight(container: Container?, constraint: Size): Float {
        return style.computedFont.measureHeight(text, style)
    }

    private fun measureString(string: String): Float = style.computedFont.measureWidth(string, style)

    private fun cloneWithNewText(newText: String) = TextLineElement(id, parent, newText, style, proxyFor ?: this)

    override fun applySplit(remainingWidth: Float, maxWidth: Float): SplitResult<TextLineElement> {
        val tokens = ArrayDeque(tokenized)

        if (tokens.isEmpty()) {
            return SplitResult.Failed()
        }

        val firstToken = tokens.first()

        if (firstToken is NewLineToken) {
            tokens.removeFirst()
            return SplitResult.NewLine(cloneWithNewText(tokens.join()))
        }

        if (firstToken is WordToken) {
            // If we cant fit the first word in the remaining space, but there would be enough space
            // in the next line - just move to the next line.
            val firstWordWidth = measureString(firstToken.word)
            if (firstWordWidth > remainingWidth && firstWordWidth < maxWidth) {
                return SplitResult.NewLine(this)
            }

            // If the first word can not fit into the full width of the container - split the word
            if (firstWordWidth > maxWidth) {
                tokens.removeFirst()
                val (wordHead, potentialWordTail) = splitWord(maxWidth, firstToken.word)
                val wordTail = potentialWordTail ?: ""
                return SplitResult.TakeNextLine(
                    newLine = cloneWithNewText(wordHead.trimEnd()),
                    remainder = cloneWithNewText(wordTail + tokens.join()),
                )
            }
        }

        // Calculate how many tokens we can fit into the remaining space
        val fittingTokens = mutableListOf<TextToken>()
        while (tokens.isNotEmpty()) {
            val currentToken = tokens.removeFirst()
            if (currentToken is NewLineToken) {
                // If there is a new line token in the middle of the text - wrap no tnext line right there
                return SplitResult.Wrap(
                    fittingPart = cloneWithNewText(fittingTokens.join().trimEnd()),
                    remainder = cloneWithNewText(tokens.join()),
                )
            }

            val currentText = (fittingTokens + currentToken).join()
            val currentWidth = measureString(currentText)

            if (currentWidth > remainingWidth) {
                val fittingText = fittingTokens.join().trimEnd()
                if (currentToken !is SeparatorToken) {
                    tokens.addFirst(currentToken)
                }
                return if (fittingText.isEmpty()) {
                    SplitResult.NewLine(cloneWithNewText(tokens.join()))
                } else {
                    SplitResult.Wrap(
                        fittingPart = cloneWithNewText(fittingTokens.join().trimEnd()),
                        remainder = cloneWithNewText(tokens.join()),
                    )
                }
            } else {
                fittingTokens.add(currentToken)
            }
        }

        return SplitResult.FitsCompletely(this)
    }

    private fun splitWord(width: Float, word: String): Pair<String, String?> {
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