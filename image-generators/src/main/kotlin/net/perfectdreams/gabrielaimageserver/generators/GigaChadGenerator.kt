package net.perfectdreams.gabrielaimageserver.generators

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.perfectdreams.gabrielaimageserver.generators.utils.GeneratorsUtils
import net.perfectdreams.gabrielaimageserver.generators.utils.ImageUtils
import net.perfectdreams.gabrielaimageserver.generators.utils.enableFontAntialiasing
import java.awt.Color
import java.awt.Font
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
import kotlin.concurrent.thread

class GigaChadGenerator(
    val font: Font,
    val tempFolder: File,
    val assetsFolder: File,
    val ffmpegPath: File
) : Generator {
    companion object {
        /**
         * Padding in the text, to avoid the text being too close to the border
         */
        private const val PADDING = 8
    }

    suspend fun generate(
        virginLine: String,
        gigachadLine: String,
    ): ByteArray {
        val outputFileName = GeneratorsUtils.generateFileName("gigachad", "mp4")
        val outputFile = File(tempFolder, outputFileName)

        val processBuilder = ProcessBuilder(
            ffmpegPath.toString(),
            "-framerate",
            "24",
            "-i",
            "-", // We will write to output stream
            "-i",
            File(assetsFolder, "gigachad.ogg").toString(),
            "-c:v",
            "libx264",
            "-preset",
            "ultrafast", // we can trade superfast to ultrafast because the video is super short, it shaves ~0.5s from the generation time
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
        // We will limit the text in 1000 chars, to avoid overloading the generator
        val text1 = generateTextWithinFrame(virginLine.take(1000))
        val text2 = generateTextWithinFrame(gigachadLine.take(1000))

        val biggerText = if (text1.height > text2.height)
            text1
        else
            text2

        // Calculate the center of the caption height, used to centralize the text1 and text2
        val centerYOfTheCaption = biggerText.height - (biggerText.height / 2)

        for (frame in 0..378) {
            // We are in a "No Edit Frame", so we are going to just copy the frame data as is instead of loading it into memory using ImageIO (and that uses a lot of memory!)
            val paddedFrames = frame.toString()
                .padStart(6, '0')

            val fileName = "frame_$paddedFrames.jpeg"

            val imageFrameFile = File(assetsFolder, "frames/$fileName")

            val imageFrame = withContext(Dispatchers.IO) {
                ImageIO.read(imageFrameFile)
            }

            val base = BufferedImage(
                imageFrame.width,
                imageFrame.height + biggerText.height,
                BufferedImage.TYPE_INT_RGB // must be rgb because bmp doesn't support alpha (argb)
            )

            val graphics = base.createGraphics()
            graphics.color = Color.WHITE
            graphics.fillRect(0, 0, base.width, base.height)

            graphics.drawImage(
                text1,
                PADDING,
                centerYOfTheCaption - (text1.height / 2)
            )

            graphics.drawImage(
                text2,
                (540 / 2) + PADDING,
                centerYOfTheCaption - (text2.height / 2)
            )

            graphics.drawImage(
                imageFrame,
                0,
                biggerText.height
            )

            val baos = ByteArrayOutputStream()

            withContext(Dispatchers.IO) {
                // BMP is waaaaay faster to write than png, so let's use it!
                // Video should be divisible by 2
                val newWidth = if (base.width % 2 == 1)
                    base.width + 1
                else base.width

                val newHeight = if (base.height % 2 == 1)
                    base.height + 1
                else base.height

                ImageIO.write(
                    ImageUtils.toBufferedImage(
                        base.getScaledInstance(
                            newWidth,
                            newHeight,
                            BufferedImage.SCALE_FAST
                        ),
                        BufferedImage.TYPE_INT_RGB,
                    ),
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

    private fun generateTextWithinFrame(input: String): BufferedImage {
        // We need to grow the frame depending if we can fit everything on it
        // We know that the max width should be 540 / 2
        // We multiply padding by two because both sides (left side and right side) should have padding
        val maxWidthWithoutPadding = (540 / 2) - (PADDING * 2)
        val centerXWithoutPadding = (maxWidthWithoutPadding / 2)

        val currentLine = StringBuilder()
        val lines = mutableListOf<TextPosition>()

        val derivedFont = font.deriveFont(
            if (8 >= input.length)
                32f
            // Decrease the font size based on the length of the input
            else (32 - ((input.length - 8) * 0.2)).toFloat().coerceAtLeast(12f)
        )

        // We will calculate if the string fits the image
        val affineTransform = AffineTransform()
        val frc = FontRenderContext(affineTransform, true, true)

        val textSplitted = input.split(" ")
        for (ch in textSplitted) {
            // We will readd the removed spaces here
            val newLine = ("$currentLine $ch")

            val newStringBounds = derivedFont.getStringBounds(newLine, frc)
            val newStringWidth = newStringBounds.width

            if (newStringWidth >= maxWidthWithoutPadding) {
                val stringBounds = derivedFont.getStringBounds(currentLine.toString(), frc)
                val stringWidth = stringBounds.width
                val stringHeight = stringBounds.height

                lines.add(
                    TextPosition(
                        stringWidth.toInt(),
                        stringHeight.toInt(),
                        currentLine.toString(),
                    )
                )
                currentLine.clear()
            }

            currentLine.append(" ")
            currentLine.append(ch)
        }

        val stringBounds = derivedFont.getStringBounds(currentLine.toString(), frc)
        val stringWidth = stringBounds.width
        val stringHeight = stringBounds.height

        lines.add(
            TextPosition(
                stringWidth.toInt(),
                stringHeight.toInt(),
                currentLine.toString(),
            )
        )

        val currentHeight = lines.sumOf { it.height }

        val frame = BufferedImage(maxWidthWithoutPadding, currentHeight + (PADDING * 2), BufferedImage.TYPE_INT_ARGB)
        val centerY = frame.height / 2
        val graphics = frame.createGraphics()
        graphics.enableFontAntialiasing()
        // We don't need to fill the background with white because that will be done when generating the frame
        graphics.font = derivedFont
        graphics.color = Color.BLACK

        var currentY = PADDING

        for (line in lines) {
            val width = line.width
            val height = line.height
            val text = line.text

            currentY += height

            graphics.drawString(
                text,
                centerXWithoutPadding - (width / 2),
                currentY
            )
        }

        return frame
    }

    private data class TextPosition(val width: Int, val height: Int, val text: String)
}