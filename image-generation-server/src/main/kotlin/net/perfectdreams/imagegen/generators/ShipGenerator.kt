package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.JVMImage
import net.perfectdreams.imageserver.utils.ImageUtils
import java.awt.Color
import java.awt.Font
import java.awt.Rectangle
import java.awt.image.BufferedImage


class ShipGenerator(
    val assets: ShipGeneratorAssets
) {
    companion object {
        private val MAX_LOVE_COLOR = HueSaturationBrightness(339f / 360, 82f  / 100, 92f / 100)
        private val MAX_SHRUG_COLOR = HueSaturationBrightness(266f / 360, 49f / 100, 79f / 100)
        private val MAX_SOB_COLOR = HueSaturationBrightness(206f / 360, 82f / 100, 80f / 100)
        private val MAX_LOST_COLOR = HueSaturationBrightness(0f / 360, 0f / 100, 5f / 100)
        private val PERCENTAGE_RECTANGLE = Rectangle(65, 253, 494, 39)
    }

    private val font = assets.font.deriveFont(30f)

    // There are multiple ship "sections"
    // There are 101 valid progress values, from 0 to 100
    // The sections is (101 - 2) / 3
    // 100: Best Ship
    // 100 + Marriage: Variation of the above
    // 0: The worst ship ever
    // Anything else: Split up in 33% chunks
    fun generate(user1Avatar: BufferedImage, user2Avatar: BufferedImage, percentage: Int): Image {
        val canvas = BufferedImage(614, 306, BufferedImage.TYPE_INT_ARGB)
        val graphics = ImageUtils.enableTextAntialiasing(canvas.createGraphics())

        val baseImage: BufferedImage
        val status: BufferedImage
        val statusCoordinates: Pair<Int, Int>
        val beginningValues: HueSaturationBrightness
        val targetValues: HueSaturationBrightness
        val sectionSubtract: Int
        val sectionLength: Float

        when (percentage) {
            100 -> {
                baseImage = assets.sparklingHeartsBase
                status = assets.sparklingHeartStatus
                statusCoordinates = Pair(184, 0)

                beginningValues = MAX_LOVE_COLOR
                targetValues = MAX_LOVE_COLOR
                sectionSubtract = 100
                sectionLength = 1f
            }
            in 67..99 -> {
                baseImage = assets.heartsBase
                status = assets.heartStatus
                statusCoordinates = Pair(184, 0)

                beginningValues = MAX_SHRUG_COLOR
                targetValues = MAX_LOVE_COLOR
                sectionSubtract = 67
                sectionLength = 33f
            }
            in 34..66 -> {
                baseImage = assets.shrugBase
                status = assets.shrugStatus
                statusCoordinates = Pair(194, 0)

                beginningValues = MAX_SOB_COLOR
                targetValues = MAX_SHRUG_COLOR
                sectionSubtract = 34
                sectionLength = 33f
            }
            in 1..33 -> {
                baseImage = assets.sobBase
                status = assets.sobStatus
                statusCoordinates = Pair(194, 0)

                beginningValues = MAX_LOST_COLOR
                targetValues = MAX_SOB_COLOR
                sectionSubtract = 1
                sectionLength = 33f
            }
            0 -> {
                baseImage = assets.skullsBase
                status = assets.skullStatus
                statusCoordinates = Pair(196, 0)

                beginningValues = MAX_LOST_COLOR
                targetValues = MAX_LOST_COLOR
                sectionSubtract = 0
                sectionLength = 1f
            }
            else -> error("Percentage is out of range")
        }

        // We need to do a deep copy because we are going to do destructive edits to it!
        val image = ImageUtils.deepCopy(baseImage)
        val time = (percentage - sectionSubtract) / sectionLength

        val diffHue = linearEasing(time, beginningValues.hue, targetValues.hue, 1f)
        val diffSaturation = linearEasing(time, beginningValues.saturation, targetValues.saturation, 1f)
        val diffBrightness = linearEasing(time, beginningValues.brightness, targetValues.brightness, 1f)

        // Optimization: Reuse the HSB array to avoid allocating new arrays
        val hsbVals = FloatArray(3)

        for (x in 0 until baseImage.width) {
            for (y in 0 until baseImage.height) {
                val color = Color(image.getRGB(x, y), true)

                // If the color alpha is zero, we don't need to process it
                if (color.alpha != 0) {
                    Color.RGBtoHSB(color.red, color.green, color.blue, hsbVals)

                    // First, we need to "neutralize" the original brightness value, to avoid replacing the background tiled images!
                    // This must be applied on the brightness change!
                    val neutralizedBrightness = hsbVals[2] - MAX_LOVE_COLOR.brightness

                    val rgb = Color.HSBtoRGB(
                        diffHue, // We won't coerce the hue, because Java automatically wraps degrees around (yay!)
                        diffSaturation.coerceIn(0.0f, 1.0f),
                        (neutralizedBrightness + diffBrightness).coerceIn(0.0f, 1.0f)
                    )

                    image.setRGB(x, y, rgb or (color.alpha shr 24))
                }
            }
        }

        // Draw the recolored base
        graphics.drawImage(image, 13, 35, null)

        // Draw the base outline
        graphics.drawImage(assets.outlineBase, -1, 21, null)

        // Draw the ship status
        graphics.drawImage(status, statusCoordinates.first, statusCoordinates.second, null)

        // ===[ AVATARS ]===
        // First we will draw the avatar wrapper background, because what if the user has a transparent background? kinda sus...
        graphics.drawImage(
            assets.avatarWrapperBackground,
            32,
            51,
            null
        )

        graphics.drawImage(
            assets.avatarWrapperBackground,
            393,
            51,
            null
        )

        // We render the images a bit more offsetted, because there is aliasing in the avatar wrapper
        // so we render it a bit "outside" where the circle should begin, with a increased width + height
        graphics.drawImage(
            ImageUtils.makeRoundedCorners(
                ImageUtils.toBufferedImage(user1Avatar.getScaledInstance(189, 189, BufferedImage.SCALE_SMOOTH)),
                999
            ),
            32,
            51,
            null
        )

        graphics.drawImage(
            ImageUtils.makeRoundedCorners(
                ImageUtils.toBufferedImage(user2Avatar.getScaledInstance(189, 189, BufferedImage.SCALE_SMOOTH)),
                999
            ),
            393,
            51,
            null
        )

        // ===[ AVATAR OUTLINE ]===
        graphics.drawImage(
            assets.avatarWrapperOutline,
            19,
            38,
            null
        )

        graphics.drawImage(
            assets.avatarWrapperOutline,
            380,
            38,
            null
        )

        // ===[ PROGRESS BAR ]===
        graphics.drawImage(
            assets.progressBarBackground,
            51,
            239,
            null
        )

        // No need to draw the percentage bar if it is 0
        if (percentage != 0) {
            val width = (494 * (percentage / 100.0)).toInt()

            graphics.drawImage(
                assets.progressBar
                    .getSubimage(0, 0, width, 39),
                65,
                253,
                null
            )
        }

        // Draw percentage text!
        graphics.color = Color.WHITE
        graphics.font = font
        ImageUtils.drawCenteredString(
            graphics,
            "$percentage%",
            PERCENTAGE_RECTANGLE
        )

        graphics.drawImage(
            assets.progressBarOverlay,
            51,
            239,
            null
        )

        graphics.drawImage(
            assets.progressBarReflection,
            66,
            254,
            null
        )

        return JVMImage(canvas)
    }

    private fun linearEasing(time: Float, begin: Float, change: Float, duration: Float): Float {
        // This will automatically set the change to "change - begin"
        val correctChange = change - begin
        return correctChange * (time / duration) + begin
    }

    private data class HueSaturationBrightness(
        val hue: Float,
        val saturation: Float,
        val brightness: Float
    )

    data class ShipGeneratorAssets(
        val sparklingHeartsBase: BufferedImage,
        val heartsBase: BufferedImage,
        val shrugBase: BufferedImage,
        val sobBase: BufferedImage,
        val skullsBase: BufferedImage,
        val outlineBase: BufferedImage,

        val sparklingHeartStatus: BufferedImage,
        val heartStatus: BufferedImage,
        val shrugStatus: BufferedImage,
        val sobStatus: BufferedImage,
        val skullStatus: BufferedImage,

        val avatarWrapperOutline: BufferedImage,
        val avatarWrapperBackground: BufferedImage,

        val progressBar: BufferedImage,
        val progressBarBackground: BufferedImage,
        val progressBarOverlay: BufferedImage,
        val progressBarReflection: BufferedImage,

        val font: Font
    )
}