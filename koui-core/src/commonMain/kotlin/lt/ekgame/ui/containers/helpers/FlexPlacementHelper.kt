package lt.ekgame.ui.containers.helpers

import lt.ekgame.ui.Placeable

class FlexPlacementHelper {
    private val buckets: MutableList<MutableList<Placeable>> = mutableListOf(mutableListOf())

    fun addBucket() {
        if (getCurrentBucket().isNotEmpty()) {
            buckets.add(mutableListOf())
        }
    }

    fun add(placeable: Placeable) = buckets.last().add(placeable)

    fun getCurrentBucket(): List<Placeable> = buckets.last()

    fun getCurrentBucketWidth(gap: Float = 0f) = getCurrentBucket().map { (it.width ?: 0f) + gap }.sum()

    fun getCurrentBucketHeight(gap: Float = 0f) = getCurrentBucket().map { (it.height ?: 0f) + gap }.sum()

    fun getNumBuckets() = buckets.size

    fun getBucket(index: Int): List<Placeable> = buckets[index]

    fun getBucketWidth(index: Int, gap: Float = 0f): Float = getBucket(index).map { (it.width ?: 0f) + gap }.sum() - gap

    fun getBucketHeight(index: Int, gap: Float = 0f): Float = getBucket(index).map { (it.height ?: 0f) + gap }.sum() - gap

    fun getBucketMaxWidth(index: Int): Float = getBucket(index).maxOfOrNull { it.width ?: 0f } ?: 0f

    fun getBucketMaxHeight(index: Int): Float = getBucket(index).maxOfOrNull { it.height ?: 0f } ?: 0f

    fun getTotalHeight(gap: Float): Float {
        val gapSize = (getNumBuckets() - 1)*gap
        return buckets.map { bucket -> bucket.maxOfOrNull { it.height ?: 0f } ?: 0f }.sum() + gapSize
    }

    fun getTotalWidth(gap: Float): Float {
        val gapSize = (getNumBuckets() - 1)*gap
        return buckets.map { bucket -> bucket.maxOfOrNull { it.width ?: 0f } ?: 0f }.sum() + gapSize
    }
}