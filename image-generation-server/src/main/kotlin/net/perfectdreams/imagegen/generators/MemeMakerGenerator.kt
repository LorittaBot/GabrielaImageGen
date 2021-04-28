package net.perfectdreams.imagegen.generators

import net.perfectdreams.imageserver.utils.extensions.enableFontAntialiasing
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.util.*

class MemeMakerGenerator(
    val font: Font
) {
    fun generate(
        image: BufferedImage,
        line1: String,
        line2: String?
    ): BufferedImage {
        val sectionFrame = generateTextWithinFrame(image, line1, line2)

        val graphics = image.createGraphics()
        graphics.drawImage(sectionFrame, 0, 0, null)

        return image
    }

    private fun generateTextWithinFrame(image: BufferedImage, line1: String, line2: String?): BufferedImage {
        val frame = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)
        generateLineWithinFrame(frame, line1, false)
        if (line2 != null)
            generateLineWithinFrame(frame, line2, true)

        return frame
    }

    private fun generateLineWithinFrame(frame: BufferedImage, line: String, inverted: Boolean) {
        // Yet another case of "calculate stuff with rule of three:tm:"
        // 420 --- 65
        // frame.width --- size

        val reservedSpaceForText = ((frame.width * 65) / 420)
        val reservedSpaceForPadding = ((frame.width * 5) / 420)

        val (positions, fontSize) = calculatePositionsOfTextWithinFrame(
            frame,
            frame.graphics,
            line,
            if (!inverted)
                reservedSpaceForText
            else
                frame.height - reservedSpaceForText,
            150,
            3,
            reservedSpaceForPadding,
            inverted
        )

        val frameGraphics = frame.createGraphics().enableFontAntialiasing()

        // Draw the Outline
        val outlineSize = ((fontSize * 3) / 65).coerceAtLeast(1) // However the minimum outline size should be 1!
        frameGraphics.color = Color.BLACK
        for (x in -outlineSize..outlineSize) {
            for (y in -outlineSize..outlineSize) {
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