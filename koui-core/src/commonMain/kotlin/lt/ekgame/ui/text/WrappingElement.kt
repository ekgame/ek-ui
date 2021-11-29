package lt.ekgame.ui.text

import lt.ekgame.ui.Element

interface WrappingElement<T : Element> {
    fun applySplit(remainingWidth: Float, maxWidth: Float): SplitResult<T>
}

sealed class SplitResult<T : Element> {
    class NewLine<T : Element>(val remainder: T) : SplitResult<T>()
    class FitsCompletely<T : Element>(val element: T) : SplitResult<T>()
    class Wrap<T : Element>(val fittingPart: T, val remainder: T) : SplitResult<T>()
    class TakeNextLine<T : Element>(val newLine: T, val remainder: T) : SplitResult<T>()
    class Failed<T : Element> : SplitResult<T>()
}
