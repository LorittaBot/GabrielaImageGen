package net.perfectdreams.gabrielaimageserver.generators.undertale.textbox

import net.perfectdreams.gabrielaimageserver.generators.Generator
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.io.ByteArrayOutputStream
import javax.imageio.stream.MemoryCacheImageOutputStream
import net.perfectdreams.gabrielaimageserver.utils.gifs.AnimatedGifEncoder
import net.perfectdreams.gabrielaimageserver.utils.gifs.palettecreators.CachedPaletteCreator
import net.perfectdreams.gabrielaimageserver.utils.gifs.palettecreators.NaiveDistancePaletteCreator

abstract class TobyBoxGenerator(
    private val dialogBoxImage: BufferedImage,
    private val font: Font
) : Generator {
    companion object {
        const val PORTRAIT_CENTER_X = 93
        const val PORTRAIT_CENTER_Y = 86

        // The font is fixed width, so we know how many characters fit in a single line!
        private const val MAX_CHARACTERS_IN_LINE_WITH_PORTRAIT = 24
        private const val MAX_CHARACTERS_IN_LINE_NO_PORTRAIT = 31
    }

    private val undertaleDialogBoxFont = font
        .deriveFont(26f)

    /**
     * List of colors that should always be present in the GIF palette, the box and text colors should be in here!
     */
    abstract val colorsThatShouldBePresent: List<Color>

    fun generate(input: String, portrait: CharacterPortrait?): ByteArray {
        val builder = StringBuilder()
        val lines = mutableListOf<String>()

        val maxCharactersInLine = if (portrait != null)
            MAX_CHARACTERS_IN_LINE_WITH_PORTRAIT
        else
            MAX_CHARACTERS_IN_LINE_NO_PORTRAIT

        val currentWord = StringBuilder()

        // Now we need to try fitting the input string into lines
        fun appendWordToBuilder() {
            var appendSpaceAtTheBeginning = builder.isNotEmpty()

            if (builder.length + currentWord.length + (if (appendSpaceAtTheBeginning) 1 else 0) > maxCharactersInLine) {
                // Won't fit...! Skip to the next line!
                if (builder.isNotEmpty()) {
                    // We check for "isNotEmpty()" because the input may be "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                    lines.add(builder.toString())
                    builder.clear()
                    appendSpaceAtTheBeginning = false // The builder was emptied, so let's change this to false
                } else {
                    // If the builder is empty, what we will do is a "forceful split"
                    // We are going to chunk the current builder and append the result
                    val chunked = currentWord.toString().chunked(maxCharactersInLine)
                    val fullLines = chunked.takeWhile { it.length == maxCharactersInLine }
                    lines.addAll(fullLines)

                    val theRestOfTheLines = chunked - fullLines
                    currentWord.append(theRestOfTheLines.joinToString(" "))
                    return
                }
            }

            if (appendSpaceAtTheBeginning)
                builder.append(' ')

            builder.append(currentWord)

            currentWord.clear()
        }

        for (ch in input) {
            if (ch == ' ') {
                appendWordToBuilder()
            } else {
                currentWord.append(ch)
            }
        }

        appendWordToBuilder()
        if (builder.isNotEmpty())
            lines.add(builder.toString())

        return generate(
            lines.take(3), // Only 3 lines can fit in the text box anyway
            portrait
        )
    }

    fun generate(lines: List<String>, portrait: CharacterPortrait?): ByteArray {
        val baos = ByteArrayOutputStream()
        val baosAsMemoryCacheImage = MemoryCacheImageOutputStream(baos)

        val gifEncoder = AnimatedGifEncoder(baos)
        gifEncoder.repeat = -1
        gifEncoder.transparent = Color.MAGENTA
        gifEncoder.start()

        // To avoid intensive palette calculations, we are going to cache the palette!
        // Because the colors won't even change between frames anyway, so don't even bother recalculating it!
        // TODO: Maybe it would be better to generate the last frame first, calculate the palette and then generate the rest of the frames!
        var cachedColorTab: ByteArray? = null

        repeat(lines.sumOf { it.length }) {
            val indexPlusOne = it + 1

            val toBeDrawn = mutableListOf<String>()

            var subtractFromTake = 0
            for (line in lines) {
                val howMuchShouldBeTaken = indexPlusOne - subtractFromTake
                if (howMuchShouldBeTaken > 0) {
                    // Only try taking what should be drawn if the value is > 0, because if it less, there will be an exception!
                    // (And because if it is less than 0, it shouldn't be drawn anyway)
                    toBeDrawn += line.take(indexPlusOne - subtractFromTake)
                    subtractFromTake += line.length
                }
            }

            val frame = generateFrame(
                toBeDrawn,
                portrait
            )

            val colorTab = cachedColorTab ?: run {
                NaiveDistancePaletteCreator.createPaletteFromRgbValues(
                    (frame.raster.dataBuffer as DataBufferByte).data,
                    // We want BLACK and WHITE to ALWAYS be present!
                    // This also gives a SUPER nice performance boost, because those palettes will be present at the beginning of the palette
                    // So the createPaletteFromRgbValues will hit those values more frequently, bringing down GIF generation speed from ~5500ms to 1000ms (sweet)!
                    colorsThatShouldBePresent,
                    16,
                    Color.MAGENTA,
                    // Less = Faster GIF generation
                    // In my tests, using 256 takes 6s to generate a text box with "owo uwu owo uwu owo uwu owo uwu owo uwu owo uwu owo uwu"
                    // (This is due to the RGBPaletteToGIFPaletteConverter)
                    192
                )
            }

            cachedColorTab = colorTab

            gifEncoder.addFrame(
                frame,
                3,
                paletteCreator = CachedPaletteCreator(colorTab)
            )
        }

        gifEncoder.finish()

        baosAsMemoryCacheImage.close()
        val endResult = baos.toByteArray()
        baos.close()

        return endResult
    }

    private fun generateFrame(toBeDrawn: List<String>, portrait: CharacterPortrait?): BufferedImage {
        val portraitImage = portrait?.image
        val hasPortrait = portraitImage != null

        val asteriskXBeginning = if (hasPortrait) 151 else 37
        val textXBeginning = asteriskXBeginning + 32

        val base = BufferedImage(
            dialogBoxImage.width,
            dialogBoxImage.height,
            // This NEEDS to be BGR, because we will cache the palette of an frame!
            // If it isn't, the code will fail while casting to DataBufferByte!
            BufferedImage.TYPE_3BYTE_BGR
        )

        val baseGraphics = base.createGraphics()
        baseGraphics.color = Color.MAGENTA
        baseGraphics.fillRect(0, 0, base.width, base.height)

        baseGraphics.drawImage(
            dialogBoxImage,
            0,
            0,
            null
        )

        if (hasPortrait && portraitImage != null) {
            val centerX = (portrait as? TobyGameCharacterPortrait)?.centerX ?: PORTRAIT_CENTER_X
            val centerY = (portrait as? TobyGameCharacterPortrait)?.centerY ?: PORTRAIT_CENTER_Y

            baseGraphics.drawImage(
                portraitImage,
                centerX - (portraitImage.getWidth(null) / 2),
                centerY - (portraitImage.getHeight(null) / 2),
                null
            )
        }

        baseGraphics.font = undertaleDialogBoxFont

        drawString(
            baseGraphics,
            "* ",
            asteriskXBeginning,
            54
        )

        var yOffset = 0
        for (line in toBeDrawn) {
            drawString(
                baseGraphics,
                line,
                textXBeginning, // 36,
                53 + yOffset
            )

            yOffset += 36
        }

        return base
    }

    abstract fun drawString(graphics2D: Graphics2D, str: String, x: Int, y: Int)
}