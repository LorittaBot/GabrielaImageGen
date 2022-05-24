package net.perfectdreams.gabrielaimageserver.generators.utils

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.awt.image.BufferedImage
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Loads keyframes from a CSV file
 */
class AEKeyframes(
    val layers: Map<String, Layer>
) {
    companion object {
        fun fromFile(file: File): AEKeyframes {
            val lines = file
                .readLines()
                .map { it.split(";") }

            return AEKeyframes(
                lines.groupBy { it[1] /* Layer Name */ }
                    .map {
                        it.key to Layer(
                            it.value.map {
                                val time = it[0].toDouble()
                                val isActive = it[2].toBoolean()

                                val positionX = it[3].toDouble()
                                val positionY = it[4].toDouble()

                                val scaleX = it[6].toDouble()
                                val scaleY = it[7].toDouble()

                                val rotation = it[9].toDouble()

                                Keyframe(
                                    time,
                                    isActive,
                                    positionX,
                                    positionY,
                                    scaleX,
                                    scaleY,
                                    rotation
                                )
                            }.sortedBy(Keyframe::time)
                        )
                    }
                    .toMap()
            )
        }
    }

    data class Keyframe(
        val time: Double,
        val isActive: Boolean,
        val positionX: Double,
        val positionY: Double,
        val scaleX: Double,
        val scaleY: Double,
        val rotation: Double
    )

    data class Layer(
        val keyframes: List<Keyframe>
    ) {
        val commonScaledKeyframes: Set<Pair<Double, Double>>

        init {
            val commonScaledInstances = keyframes.groupBy { Pair(it.scaleX, it.scaleY) }
                .entries
                .filter { it.value.size > 1 }

            commonScaledKeyframes = commonScaledInstances.map { it.key }.toSet()
        }

        fun getExclusiveGenerationLayerInstance() = ExclusiveGenerationLayer(this)
    }

    data class ExclusiveGenerationLayer(
        val layer: Layer
    ) {
        val keyframes by layer::keyframes
        val commonScaledKeyframes by layer::commonScaledKeyframes
        private val cachedScaledImages = mutableMapOf<Pair<Double, Double>, BufferedImage>()
        private val pairMutexes = ConcurrentHashMap<Pair<Double, Double>, Mutex>()

        fun scaleImageOrGetCached(bufferedImage: BufferedImage, scaleX: Double, scaleY: Double): BufferedImage {
            val keyframePair = Pair(scaleX, scaleY)
            val cachedScaledImage = cachedScaledImages[keyframePair]
            if (cachedScaledImage != null)
                return cachedScaledImage

            val width = (bufferedImage.width.toDouble() * (scaleX / 100)).toInt()
            val height = (bufferedImage.height.toDouble() * (scaleY / 100)).toInt()

            // getScaledInstance takes around ~30ms to scale the "9" keyframes
            // This however is WAY faster!
            val scaledNumber = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

            val g = scaledNumber.createGraphics()
            g.drawImage(bufferedImage, 0, 0, width, height, null)
            g.dispose()

            // Only cache if this image is going to be reused later
            if (layer.commonScaledKeyframes.contains(keyframePair))
                cachedScaledImages[keyframePair] = scaledNumber

            return scaledNumber
        }

        suspend fun scaleImageOrGetCachedAsync(bufferedImage: BufferedImage, scaleX: Double, scaleY: Double) = pairMutexes.getOrPut(Pair(scaleX, scaleY)) { Mutex() }.withLock {
            scaleImageOrGetCached(bufferedImage, scaleX, scaleY)
        }
    }
}