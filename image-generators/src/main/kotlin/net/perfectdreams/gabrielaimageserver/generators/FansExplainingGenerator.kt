package net.perfectdreams.gabrielaimageserver.generators

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.perfectdreams.gabrielaimageserver.generators.utils.GeneratorsUtils
import net.perfectdreams.gabrielaimageserver.generators.utils.NoCopyByteArrayOutputStream
import net.perfectdreams.gabrielaimageserver.generators.utils.enableFontAntialiasing
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.concurrent.thread

class FansExplainingGenerator(
    val font: Font,
    val tempFolder: File,
    val assetsFolder: File,
    val ffmpegPath: File
) : Generator {
    companion object {
        private val FRAME_RANGE = 0..484
        private val SECTION_1_FRAMES = 0..83
        private val SECTION_2_FRAMES = 84..180
        private val SECTION_3_FRAMES = 181..274
        private val SECTION_4_FRAMES = 275..367
        private val SECTION_5_FRAMES = 368..484
    }

    suspend fun generate(
        section1Line1: String,
        section1Line2: String,
        section2Line1: String,
        section2Line2: String,
        section3Line1: String,
        section3Line2: String,
        section4Line1: String,
        section4Line2: String,
        section5Line1: String,
        section5Line2: String,
    ): ByteArray {
        val outputFileName = GeneratorsUtils.generateFileName("fans-explaining", "mp4")
        val outputFile = File(tempFolder, outputFileName)

        val processBuilder = ProcessBuilder(
            ffmpegPath.toString(),
            "-framerate",
            "30",
            "-i",
            "-", // We will write to output stream
            "-i",
            File(assetsFolder, "ppg_theme.ogg").toString(),
            "-c:v",
            "libx264",
            "-preset",
            "superfast",
            "-pix_fmt",
            "yuv420p",
            "-y",
            // Due to the way MP4 containers work (it goes back after writing all data!), we need to write directly to a file
            outputFile.toString()
        ).redirectErrorStream(true)
            .start()

        thread {
            while (true) {
                val r = processBuilder.inputStream.read()
                if (r == -1)
                // Keep reading until end of input
                    return@thread

                print(r.toChar())
            }
        }

        // We are going to pre-generate the text, this way we avoid recreating the text within the loop (which would be expensive!)
        // Also the text doesn't change, so it is better to do it this way :)
        val section1Frame = generateTextWithinFrame(section1Line1, section1Line2)
        val section2Frame = generateTextWithinFrame(section2Line1, section2Line2)
        val section3Frame = generateTextWithinFrame(section3Line1, section3Line2)
        val section4Frame = generateTextWithinFrame(section4Line1, section4Line2)
        val section5Frame = generateTextWithinFrame(section5Line1, section5Line2)

        for (frame in FRAME_RANGE) {
            // We are in a "No Edit Frame", so we are going to just copy the frame data as is instead of loading it into memory using ImageIO (and that uses a lot of memory!)
            val paddedFrames = frame.toString()
                .padStart(6, '0')

            val fileName = "frame_$paddedFrames.jpeg"

            val imageFrameFile = File(assetsFolder, "frames/$fileName")

            val imageFrame = withContext(Dispatchers.IO) {
                ImageIO.read(imageFrameFile)
            }

            val graphics = imageFrame.createGraphics()
                .enableFontAntialiasing()

            graphics.font = font

            if (frame in SECTION_1_FRAMES) {
                graphics.drawImage(section1Frame, 0, 0, null)
            }

            if (frame in SECTION_2_FRAMES) {
                graphics.drawImage(section2Frame, 0, 0, null)
            }

            if (frame in SECTION_3_FRAMES) {
                graphics.drawImage(section3Frame, 0, 0, null)
            }

            if (frame in SECTION_4_FRAMES) {
                graphics.drawImage(section4Frame, 0, 0, null)
            }

            if (frame in SECTION_5_FRAMES) {
                graphics.drawImage(section5Frame, 0, 0, null)
            }

            val derivedFont = font.deriveFont(92f)
            graphics.font = derivedFont

            val baos = NoCopyByteArrayOutputStream()

            withContext(Dispatchers.IO) {
                // BMP is waaaaay faster to write than png, so let's use it!
                ImageIO.write(
                    imageFrame,
                    "bmp",
                    baos
                )
            }

            // Write to ffmpeg output
            withContext(Dispatchers.IO) {
                processBuilder.outputStream.write(baos.toByteArray())
                processBuilder.outputStream.flush()
            }
        }

        val now = System.currentTimeMillis()

        println("Took ${System.currentTimeMillis() - now}ms to generate the frames!")

        println("Close...")
        processBuilder.outputStream.close()

        println("Waiting for ffmpeg...")
        processBuilder.waitFor()

        println("Finished!")
        println("Total: ${System.currentTimeMillis() - now}ms")

        return GeneratorsUtils.readBytesAndDelete(outputFile)
    }

    private fun generateTextWithinFrame(line1: String, line2: String): BufferedImage {
        val frame = BufferedImage(420, 420, BufferedImage.TYPE_INT_ARGB)
        generateLineWithinFrame(frame, line1, false)
        generateLineWithinFrame(frame, line2, true)

        return frame
    }

    private fun generateLineWithinFrame(frame: BufferedImage, line: String, inverted: Boolean) {
        val (positions, fontSize) = calculatePositionsOfTextWithinFrame(
            frame,
            frame.graphics,
            line,
            if (!inverted)
                65
            else
                frame.height - 65,
            150,
            25,
            5,
            inverted
        )

        val frameGraphics = frame.createGraphics().enableFontAntialiasing()

        // Draw the Outline
        frameGraphics.color = Color.BLACK
        for (x in -3..3) {
            for (y in -3..3) {
                for (position in positions) {
                    frameGraphics.font = font.deriveFont(fontSize.toFloat())
                    frameGraphics.drawString(position.text, position.x + x, position.y + y)
                }
            }
        }

        // Draw the text
        frameGraphics.color = Color.WHITE
        for (position in positions) {
            frameGraphics.font = font.deriveFont(fontSize.toFloat())
            frameGraphics.drawString(position.text, position.x, position.y)
        }
    }

    /**
     * Finds the positions of the text within the frame by trying multiple font sizes until a font size
     * satisfies all the requirements. (Or uses [minFontSize] if it wasn't possible to satisfy all constraints)
     *
     * @param frame         the frame of the image
     * @param graphics      the graphics of the image
     * @param text          the text that is going to be written
     * @param maxY          the max Y position of the text
     * @param maxFontSize   the maximum font size that is able to be used
     * @param minFontSize   the minium font size that is able to be used
     * @param borderPadding padding between the borders
     * @param inverted      if the text should be written from the bottom to top
     */
    fun calculatePositionsOfTextWithinFrame(
        frame: BufferedImage,
        graphics: Graphics,
        text: String,
        maxY: Int,
        maxFontSize: Int,
        minFontSize: Int,
        borderPadding: Int,
        inverted: Boolean
    ): Pair<List<TextPosition>, Int> {
        val centerX = frame.width / 2
        var matchedLines = listOf<TextPosition>()
        var matchedSize = 999

        sizeLoop@for (size in maxFontSize downTo minFontSize) {
            val skipIfDoesntFit = size != minFontSize

            val currentLine = mutableListOf<String>()
            val lines = mutableListOf<TextPosition>()

            // We are going to try one by one until one of them fits
            val newSizeFont = font.deriveFont(size.toFloat())
            graphics.font = newSizeFont

            val textSplitted = text.split(" ").dropLastWhile { it.isEmpty() }

            val fontMetrics = graphics.getFontMetrics(newSizeFont)

            var currentY = if (!inverted) {
                fontMetrics.maxAscent + borderPadding
            } else { frame.height - borderPadding }

            // Sometimes the font is already way too big, so let's just exit out
            if (!inverted) {
                // Overflowing bye
                if ((currentY + borderPadding) > maxY && skipIfDoesntFit)
                    continue@sizeLoop
            } else {
                // Overflowing bye
                if (maxY > (currentY - borderPadding - fontMetrics.maxAscent) && skipIfDoesntFit)
                    continue@sizeLoop
            }

            for (split in textSplitted) {
                // First we will clone the current line
                val newCurrentLine = currentLine.toMutableList()
                newCurrentLine.add(split) // And add our new text, this is going to be used for *calculations*

                // We will also calculate the current line stats
                val joinedCurrentLine = currentLine.joinToString(" ")
                val currentStringWidth = fontMetrics.stringWidth(currentLine.joinToString(" "))

                // And calculate the new current line stats
                val joinedNewCurrentLine = newCurrentLine.joinToString(" ")
                val stringWidth = fontMetrics.stringWidth(joinedNewCurrentLine)
                val stringWidthByMyself = fontMetrics.stringWidth(split)

                if (stringWidth + borderPadding > frame.width || stringWidthByMyself + borderPadding > frame.width) {
                    // Oh no, it doens't fit!
                    // This ain't gonna fit bye
                    if (currentLine.isEmpty() && skipIfDoesntFit)
                        continue@sizeLoop // If the current line is empty, try another font size!

                    // Add the current line stats
                    lines.add(TextPosition(centerX - (currentStringWidth / 2), currentY, joinedCurrentLine)) // Create a copy
                    currentLine.clear() // Clear current line

                    // And jump a new line!
                    if (!inverted) {
                        currentY += fontMetrics.maxAscent

                        // Overflowing bye
                        if ((currentY + borderPadding) > maxY && skipIfDoesntFit)
                            continue@sizeLoop
                    } else {
                        currentY -= fontMetrics.maxAscent

                        // Overflowing bye
                        if (maxY > (currentY - borderPadding) && skipIfDoesntFit)
                            continue@sizeLoop
                    }
                }

                // It... worked? Yay!
                currentLine.add(split)
            }

            // If we got to here, then it means that everything fit correctly, owo!

            // So now we are going to calculate stats (again)
            val joinedCurrentLine = currentLine.joinToString(" ")
            val currentStringWidth = fontMetrics.stringWidth(currentLine.joinToString(" "))

            // And add the current line stats...
            lines.add(TextPosition(centerX - (currentStringWidth / 2), currentY, joinedCurrentLine)) // Create a copy

            // And store our newly found data, yay!
            matchedLines = if (!inverted)
                lines
            else {
                // I hate this, we need to invert the Y position of the text
                // And I'm too lazy to change the algorithm to avoid this
                // So we are going to do it the workaround way
                val invertedList = mutableListOf<TextPosition>()

                val reversedTextPositions = lines.reversed()
                for ((index, textPosition) in lines.withIndex()) {
                    val textPositionReversed = reversedTextPositions[index]
                    invertedList.add(
                        TextPosition(
                            textPosition.x,
                            textPositionReversed.y,
                            textPosition.text
                        )
                    )
                }

                invertedList
            }
            matchedSize = size
            break // And a smol break here to exit this loop
        }

        return Pair(matchedLines, matchedSize)
    }

    data class TextPosition(val x: Int, val y: Int, val text: String)
}