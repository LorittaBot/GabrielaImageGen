package net.perfectdreams.gabrielaimageserver.generators

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import mu.KotlinLogging
import net.perfectdreams.gabrielaimageserver.exceptions.InvalidChavesOpeningTextException
import net.perfectdreams.gabrielaimageserver.generators.utils.AEKeyframes
import net.perfectdreams.gabrielaimageserver.generators.utils.GeneratorsUtils
import net.perfectdreams.gabrielaimageserver.generators.utils.enableFontAntialiasing
import net.perfectdreams.gabrielaimageserver.generators.utils.toBufferedImage
import net.perfectdreams.gabrielaimageserver.utils.Easings
import java.awt.AlphaComposite
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.GradientPaint
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.io.File
import javax.imageio.ImageIO
import kotlin.concurrent.thread


class ChavesOpeningGenerator(
    val tempFolder: File,
    val assetsFolder: File,
    val ffmpegPath: File,
    val carterOneRegularBase: Font
) : Generator {
    companion object {
        private const val CANVAS_WIDTH = 960
        private const val CANVAS_HEIGHT = 720

        private const val FRAMERATE = 30
        private const val DURATION_IN_SECONDS = 33
        private const val DURATION_IN_FRAMES = FRAMERATE * DURATION_IN_SECONDS

        private val CIRCLE_2_CLIP = Ellipse2D.Float((CANVAS_WIDTH / 2) - 335f, (CANVAS_HEIGHT / 2) - 335f, 335f + 335f, 335f + 335f)
        private val CIRCLE_3_CLIP = Ellipse2D.Float((CANVAS_WIDTH / 2) - 170f, (CANVAS_HEIGHT / 2) - 170f, 170f + 170f, 170f + 170f)
        private val BASIC_STROKE = BasicStroke(14f)
        private const val ROTATION_PER_FRAME = 6

        private val BLUE1 = Color(42, 176, 255)
        private val BLUE2 = Color(15, 71, 236)
        private val BLUE3 = Color(34, 126, 255)
        private val BLUE4 = Color(4, 0, 214)

        private val SHADOW_RGB = Color(0, 0, 0, 80).rgb

        private val GOOD_CANDIATES_FOR_CHAVES_HAT = setOf(
            'a',
            'e',
            'o',
            's',
            'q',
            'd',
            'g',
            'ç'
        )

        private val TV_BUG_COMPOSITE = AlphaComposite.SrcOver.derive(0.3f)

        private val AFFINE_TRANSFORM = AffineTransform()
        private val FONT_RENDER_CONTEXT = FontRenderContext(AFFINE_TRANSFORM, true, true)

        private const val CHAVES_LOGO_PADDING = 10

        private val logger = KotlinLogging.logger {}
    }

    private val diamondFrames = (1..284).map {
        ImageIO.read(File(assetsFolder, "diamond/${it.toString().padStart(4, '0')}.png"))
    }

    private val backgroundFrames = (0 until 360 step ROTATION_PER_FRAME).map {
        val base = BufferedImage(960, 720, BufferedImage.TYPE_3BYTE_BGR)
        val baseGraphics = base.createGraphics()
        baseGraphics.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )

        val defaultTransform = baseGraphics.transform

        // This is WAY FASTER than rendering to a BufferedImage just to paste the rotated version
        fun renderBackground() {
            val halfWidth = base.width / 2
            val halfHeight = base.height / 2

            baseGraphics.color = BLUE1
            baseGraphics.fillRect(-500, -500, halfWidth + 500, halfHeight + 500)

            baseGraphics.color = BLUE2
            baseGraphics.fillRect(halfWidth, -500, halfWidth + 500, halfHeight + 500)

            baseGraphics.color = BLUE3
            baseGraphics.fillRect(halfWidth, halfHeight, halfWidth + 500, halfHeight + 500)

            baseGraphics.color = BLUE4
            baseGraphics.fillRect(-500, halfHeight, halfWidth + 500, halfHeight + 500)
        }

        // Render it!!
        baseGraphics.rotate(Math.toRadians(it.toDouble()), base.width / 2.0, base.height / 2.0)
        renderBackground()

        baseGraphics.clip = CIRCLE_2_CLIP

        // Render it AGAIN (but before we are going to reset the transform)
        baseGraphics.transform = defaultTransform
        baseGraphics.rotate(Math.toRadians(360.0 - it), base.width / 2.0, base.height / 2.0)
        renderBackground()

        baseGraphics.clip = CIRCLE_3_CLIP

        // RENDER IT AGAIN!!
        baseGraphics.transform = defaultTransform
        baseGraphics.rotate(Math.toRadians(it.toDouble()), base.width / 2.0, base.height / 2.0)
        renderBackground()

        // Revert the clip and transform
        baseGraphics.clip = null
        baseGraphics.transform = defaultTransform

        // Add the lines and circles
        baseGraphics.color = Color(108, 238, 239)
        baseGraphics.fillRect(0, (base.height / 2) - 7, base.width, 14)
        baseGraphics.fillRect((base.width / 2) - 7, 0, 14, base.height)

        val defaultStroke = baseGraphics.stroke

        baseGraphics.stroke = BASIC_STROKE
        baseGraphics.drawOval(CIRCLE_3_CLIP.x.toInt(), CIRCLE_3_CLIP.y.toInt(), CIRCLE_3_CLIP.width.toInt(), CIRCLE_3_CLIP.height.toInt())
        baseGraphics.drawOval(CIRCLE_2_CLIP.x.toInt(), CIRCLE_2_CLIP.y.toInt(), CIRCLE_2_CLIP.width.toInt(), CIRCLE_2_CLIP.height.toInt())

        baseGraphics.stroke = defaultStroke

        base
    }

    private val keyframeManager = AEKeyframes.fromFile(File(assetsFolder, "chaves-intro-keyframes.csv"))

    private val tenLayer = keyframeManager.layers["10"]!!
    private val nineLayer = keyframeManager.layers["9"]!!
    private val eightLayer = keyframeManager.layers["8"]!!
    private val sevenLayer = keyframeManager.layers["7"]!!
    private val sixLayer = keyframeManager.layers["6"]!!
    private val fiveLayer = keyframeManager.layers["5"]!!
    private val fourLayer = keyframeManager.layers["4"]!!
    private val threeLayer = keyframeManager.layers["3"]!!
    private val twoLayer = keyframeManager.layers["2"]!!

    private val chiquinhaLayer = keyframeManager.layers["Chiquinha"]!!
    private val girafalesLayer = keyframeManager.layers["Girafales"]!!
    private val bruxaDo71Layer = keyframeManager.layers["Bruxa do 71"]!!
    private val quicoLayer = keyframeManager.layers["Quico"]!!
    private val florindaLayer = keyframeManager.layers["Florinda"]!!
    private val seuMadrugaLayer = keyframeManager.layers["Seu Madruga"]!!
    private val srBarrigaLayer = keyframeManager.layers["Sr. Barriga"]!!
    private val chavesLayer = keyframeManager.layers["Chaves"]!!

    private val ten = ImageIO.read(File(assetsFolder, "10_best.png"))
    private val nine = ImageIO.read(File(assetsFolder, "9_best.png"))
    private val eight = ImageIO.read(File(assetsFolder, "8_best.png"))
    private val seven = ImageIO.read(File(assetsFolder, "7_best.png"))
    private val six = ImageIO.read(File(assetsFolder, "6_best.png"))
    private val five = ImageIO.read(File(assetsFolder, "5_best.png"))
    private val four = ImageIO.read(File(assetsFolder, "4_best.png"))
    private val three = ImageIO.read(File(assetsFolder, "3_best.png"))
    private val two = ImageIO.read(File(assetsFolder, "2_best.png"))

    private val hat = ImageIO.read(File(assetsFolder, "hat.png"))
        .getScaledInstance(512, 512, BufferedImage.SCALE_SMOOTH)
        .toBufferedImage()

    private val hatBackground = ImageIO.read(File(assetsFolder, "hat_background.png"))
        .getScaledInstance(512, 512, BufferedImage.SCALE_SMOOTH)
        .toBufferedImage()

    private val tvBug = ImageIO.read(File(assetsFolder, "tv_bug.png"))
        .getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH)
        .toBufferedImage()

    suspend fun generate(
        chiquinha: BufferedImage,
        girafales: BufferedImage,
        bruxa: BufferedImage,
        quico: BufferedImage,
        florinda: BufferedImage,
        madruga: BufferedImage,
        barriga: BufferedImage,
        chaves: BufferedImage,
        text: String
    ): ByteArray {
        // TODO: Implement parallel frame processing? Maybe this could help speed up the video, however it looks like ffmpeg would just not keep up
        // TODO: Check if we can speed up ffmpeg, without ffmpeg processing the video takes only 9.3138694s!
        // TODO: Cut the video length in the ogg
        val text = text.take(100)

        val tenLayer = tenLayer.getExclusiveGenerationLayerInstance()
        val nineLayer = nineLayer.getExclusiveGenerationLayerInstance()
        val eightLayer = eightLayer.getExclusiveGenerationLayerInstance()
        val sevenLayer = sevenLayer.getExclusiveGenerationLayerInstance()
        val sixLayer = sixLayer.getExclusiveGenerationLayerInstance()
        val fiveLayer = fiveLayer.getExclusiveGenerationLayerInstance()
        val fourLayer = fourLayer.getExclusiveGenerationLayerInstance()
        val threeLayer = threeLayer.getExclusiveGenerationLayerInstance()
        val twoLayer = twoLayer.getExclusiveGenerationLayerInstance()

        val chiquinhaLayer = chiquinhaLayer.getExclusiveGenerationLayerInstance()
        val girafalesLayer = girafalesLayer.getExclusiveGenerationLayerInstance()
        val bruxaDo71Layer = bruxaDo71Layer.getExclusiveGenerationLayerInstance()
        val quicoLayer = quicoLayer.getExclusiveGenerationLayerInstance()
        val florindaLayer = florindaLayer.getExclusiveGenerationLayerInstance()
        val seuMadrugaLayer = seuMadrugaLayer.getExclusiveGenerationLayerInstance()
        val srBarrigaLayer = srBarrigaLayer.getExclusiveGenerationLayerInstance()
        val chavesLayer = chavesLayer.getExclusiveGenerationLayerInstance()

        val chiquinha = chiquinha
            .getScaledInstance(512, 512, BufferedImage.SCALE_SMOOTH)
            .toBufferedImage()
        val girafales = girafales
            .getScaledInstance(512, 512, BufferedImage.SCALE_SMOOTH)
            .toBufferedImage()
        val bruxa = bruxa
            .getScaledInstance(512, 512, BufferedImage.SCALE_SMOOTH)
            .toBufferedImage()
        val quico = quico
            .getScaledInstance(512, 512, BufferedImage.SCALE_SMOOTH)
            .toBufferedImage()
        val florinda = florinda
            .getScaledInstance(512, 512, BufferedImage.SCALE_SMOOTH)
            .toBufferedImage()
        val madruga = madruga
            .getScaledInstance(512, 512, BufferedImage.SCALE_SMOOTH)
            .toBufferedImage()
        val barriga = barriga
            .getScaledInstance(512, 512, BufferedImage.SCALE_SMOOTH)
            .toBufferedImage()
        val chaves = chaves
            .getScaledInstance(512, 512, BufferedImage.SCALE_SMOOTH)
            .toBufferedImage()

        val indexOfTheChavesHat = getCharacterIndexCandidateForChavesHat(text)
        val hatChar = text[indexOfTheChavesHat]

        val carterOneRegular = findAppropriateFont(text)

        val pixelBoundsOfTheFullText = getPixelBoundsForText(carterOneRegular, text)
        val pixelBoundsOfTheChavesHatCandidate = getPixelBoundsForText(carterOneRegular, hatChar.toString())

        val hatCharacterHeight = pixelBoundsOfTheChavesHatCandidate.height
        val hatCharacterWidth = pixelBoundsOfTheChavesHatCandidate.width

        // The "opening" of the hat is 198x198
        // The hat itself is 360x416
        // So the hat needs to be multiplied to 1.55 to get the proper size
        // width --- newWidth
        // height --- newHeight
        val newWidth = hatCharacterWidth * 1.55
        val newHeight = (hat.height * newWidth) / hat.width
        val multiplier = (newWidth / hat.width)

        // Then we paste it offset by 91, 200
        // But we need to multiply it again
        val offsetX = 91 * multiplier
        val offsetY = 200 * multiplier

        val newWidthAsInt = newWidth.toInt()
        val newHeightAsInt = newHeight.toInt()

        // If the width or height is 0, then the scaled hat code would fail because it can't scale something to zero width or height!
        if (newWidthAsInt == 0 || newHeightAsInt == 0)
            throw InvalidChavesOpeningTextException()

        val scaledHatBackground = hatBackground.getScaledInstance(
            newWidthAsInt,
            newHeightAsInt,
            BufferedImage.SCALE_SMOOTH
        ).toBufferedImage()

        val scaledHat = hat.getScaledInstance(
            newWidthAsInt,
            newHeightAsInt,
            BufferedImage.SCALE_SMOOTH
        ).toBufferedImage()

        val chavesLogo = createLogo(
            text,
            pixelBoundsOfTheFullText,
            carterOneRegular
        )

        val positionOfTheCharacterForTheChavesHat = getPixelBoundsForText(carterOneRegular, text.take(indexOfTheChavesHat))
            .width

        var currentRotation = 0.0
        var currentTime = 0.0

        val outputFileName = GeneratorsUtils.generateFileName("chaves-opening", "mp4")
        val outputFile = File(tempFolder, outputFileName)

        val processBuilder = ProcessBuilder(
            ffmpegPath.toString(),
            "-framerate",
            "30",
            "-f",
            "rawvideo",
            "-pixel_format",
            "bgr24", // This is what the "BufferedImage.TYPE_3BYTE_BGR" uses behind the scenes
            "-video_size",
            "${CANVAS_WIDTH}x${CANVAS_HEIGHT}",
            "-i",
            "-", // We will write to output stream
            "-i",
            File(assetsFolder, "chaves.ogg").toString(),
            "-c:v",
            "libx264",
            "-preset",
            "superfast",
            "-pix_fmt",
            "yuv420p",
            "-vf",
            "scale=640:-1",
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

        // capacity = How many frames can be buffered until we suspend the image generation step
        // We will use 8 because (((920 + 760) * 4) * 8) = ~51Mib
        val channel = Channel<BufferedImage>(8)

        var keyframesTimings = 0L
        var writingTimings = 0L

        val job = GlobalScope.launch(Dispatchers.IO) {
            for (image in channel) {
                val start = System.currentTimeMillis()

                // Send the rgb map directly to ffmpeg, don't try converting it to bmp using ImageIO because ImageIO is slooooooow!
                val rgb = (image.raster.dataBuffer as DataBufferByte).data

                writingTimings += (System.currentTimeMillis() - start)

                // Write to ffmpeg output
                processBuilder.outputStream.write(rgb)
                processBuilder.outputStream.flush()
            }
        }

        val beginning = System.currentTimeMillis()

        val frameRenderJobs = (0 until DURATION_IN_FRAMES).map {
            val cr = currentRotation
            val ct = currentTime

            val frameRenderJob = GlobalScope.async(start = CoroutineStart.LAZY) {
                val base = BufferedImage(960, 720, BufferedImage.TYPE_3BYTE_BGR)
                val baseGraphics = base.createGraphics()
                baseGraphics.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                )
                val y = backgroundFrames[((cr % 360.0) / ROTATION_PER_FRAME).toInt()]
                baseGraphics.drawImage(y, 0, 0, null)

                if (ct >= 23.0) {
                    // LOGO
                    val currentLogoFrame = (((ct - 23.0) / 4.71666666667) * 283).toInt().coerceAtMost(283)
                    val logoFrame = diamondFrames[currentLogoFrame]

                    baseGraphics.drawImage(logoFrame, 0, 0, null)

                    if (ct >= 25.0) {
                        baseGraphics.font = carterOneRegular
                        val fontMetrics = baseGraphics.fontMetrics

                        val widthOfChars = text.map {
                            it to fontMetrics.charWidth(it)
                        }

                        val textWidth = widthOfChars.sumOf { it.second }

                        baseGraphics.color = Color(255, 255, 111)

                        val height = pixelBoundsOfTheFullText.height

                        val middleX = (base.width / 2.0)
                        val middleY = (base.height / 2.0)

                        val logoPositionX = (middleX - (textWidth / 2.0) - (CHAVES_LOGO_PADDING / 2.0)).toInt()
                        val logoPositionY = (middleY - (height / 2.0) - (CHAVES_LOGO_PADDING / 2.0)).toInt()

                        val hatPositionY = (logoPositionY + chavesLogo.height - hatCharacterHeight - offsetY.toInt())

                        // Draw hat background
                        if (ct >= 28.0) {
                            baseGraphics.drawImage(
                                scaledHatBackground,
                                (logoPositionX + positionOfTheCharacterForTheChavesHat - offsetX).toInt(),
                                (-scaledHat.height + ((hatPositionY + scaledHat.height) * Easings.easeOutSine(
                                    ((ct - 28.0) / 2.0).coerceAtMost(
                                        1.0
                                    )
                                ))).toInt(),
                                null
                            )
                        }

                        // Draw logo
                        baseGraphics.drawImage(
                            chavesLogo,
                            logoPositionX - CHAVES_LOGO_PADDING,
                            // We add the height because the logo is moving from *outside* of the screen
                            (-chavesLogo.height + ((logoPositionY + chavesLogo.height) * Easings.easeInOutSine(
                                ((ct - 25.0) / 2.0).coerceAtMost(
                                    1.0
                                )
                            ))).toInt(),
                            null
                        )

                        // Draw hat foreground
                        if (ct >= 28.0) {
                            baseGraphics.drawImage(
                                scaledHat,
                                (logoPositionX + positionOfTheCharacterForTheChavesHat - offsetX).toInt(),
                                (-scaledHat.height + ((hatPositionY + scaledHat.height) * Easings.easeOutSine(
                                    ((ct - 28.0) / 2.0).coerceAtMost(
                                        1.0
                                    )
                                ))).toInt(),
                                null
                            )
                        }
                    }
                } else {
                    suspend fun renderBasedOnKeyFrameSource(
                        layerName: String,
                        source: BufferedImage,
                        layer: AEKeyframes.ExclusiveGenerationLayer
                    ) {
                        val s = System.currentTimeMillis()

                        val (_, isActive, positionX, positionY, scaleX, scaleY, rotation) = layer.keyframes.first {
                            it.time >= ct
                        }

                        if (!isActive) // Not active, so just ignore!
                            return

                        val scaledImage = layer.scaleImageOrGetCachedAsync(source, scaleX, scaleY)

                        val shadow = BufferedImage(scaledImage.width, scaledImage.height, BufferedImage.TYPE_INT_ARGB)
                        shadow.createGraphics().drawImage(scaledImage, 0, 0, null)

                        for (x in 0 until shadow.width) {
                            for (y in 0 until shadow.height) {
                                // By doing it this way, we can avoid creating unnecessary "Color" object instances!
                                val packedRGB = shadow.getRGB(x, y)
                                val alpha = packedRGB shr 24 and 255
                                if (alpha != 0) {
                                    shadow.setRGB(x, y, SHADOW_RGB)
                                }
                            }
                        }

                        val defaultTransform = baseGraphics.transform
                        // Rotating via Transform is way faster than using LorittaImage, and it avoids creating a intermediary image (sweet!)
                        baseGraphics.rotate(Math.toRadians(rotation), positionX, positionY)

                        // Shadow
                        baseGraphics.drawImage(
                            shadow,
                            (positionX - (scaledImage.getWidth(null) / 2)).toInt() - 16,
                            (positionY - (scaledImage.getHeight(null) / 2)).toInt() + 16,
                            null
                        )

                        baseGraphics.drawImage(
                            scaledImage,
                            (positionX - (scaledImage.getWidth(null) / 2)).toInt(),
                            (positionY - (scaledImage.getHeight(null) / 2)).toInt(),
                            null
                        )
                        baseGraphics.transform = defaultTransform

                        keyframesTimings += System.currentTimeMillis() - s
                    }

                    renderBasedOnKeyFrameSource("10", ten, tenLayer)
                    renderBasedOnKeyFrameSource("9", nine, nineLayer)
                    renderBasedOnKeyFrameSource("8", eight, eightLayer)
                    renderBasedOnKeyFrameSource("7", seven, sevenLayer)
                    renderBasedOnKeyFrameSource("6", six, sixLayer)
                    renderBasedOnKeyFrameSource("5", five, fiveLayer)
                    renderBasedOnKeyFrameSource("4", four, fourLayer)
                    renderBasedOnKeyFrameSource("3", three, threeLayer)
                    renderBasedOnKeyFrameSource("2", two, twoLayer)
                    renderBasedOnKeyFrameSource("Chiquinha", chiquinha, chiquinhaLayer)
                    renderBasedOnKeyFrameSource("Girafales", girafales, girafalesLayer)
                    renderBasedOnKeyFrameSource("Bruxa do 71", bruxa, bruxaDo71Layer)
                    renderBasedOnKeyFrameSource("Quico", quico, quicoLayer)
                    renderBasedOnKeyFrameSource("Florinda", florinda, florindaLayer)
                    renderBasedOnKeyFrameSource("Seu Madruga", madruga, seuMadrugaLayer)
                    renderBasedOnKeyFrameSource("Sr. Barriga", barriga, srBarrigaLayer)
                    renderBasedOnKeyFrameSource("Chaves", chaves, chavesLayer)
                }

                // Draw the TV bug
                val defaultComposite = baseGraphics.composite
                baseGraphics.composite = TV_BUG_COMPOSITE
                baseGraphics.drawImage(
                    tvBug,
                    960 - tvBug.width - 16,
                    720 - tvBug.height - 16,
                    null
                )
                baseGraphics.composite = defaultComposite

                base
            }

            currentRotation += ROTATION_PER_FRAME
            currentTime += 0.03333333333

            frameRenderJob
        }.toMutableList()

        // How do we parallelize this?
        // Because all frames must be in order, we first create all coroutines but lazy initialized (so they don't magically start)
        // Then, we go step by step each frame, rendering frames in parallel but submitting them to the channel in sequence
        repeat(DURATION_IN_FRAMES) {
            // To speed this up, we need to send to the ffmpeg in order WHEN THEY ARE FINISHED, not when All the frames of this batch are finished
            // Because let's suppose we have four frames:
            // Frame1 took 10ms
            // Frame2 took 10ms
            // Frame3 took 50ms
            // Frame4 took 10ms
            // We would need to wait 50ms just to process all the four frames, when we could send Frame1 and Frame2 to ffmpeg
            val toBeQueuedJobs = frameRenderJobs.take(4)
            toBeQueuedJobs.forEach { it.start() }

            val currentRenderJob = toBeQueuedJobs[0]

            val image = currentRenderJob.await()

            channel.send(image)

            // We remove it from the job list to let Java garbage collect the frame :3
            frameRenderJobs.removeAt(0)
        }

        frameRenderJobs.awaitAll()

        logger.info { "Timings:" }
        logger.info { "Everything: ${System.currentTimeMillis() - beginning}ms" }
        logger.info { "Time spent processing keyframes: ${keyframesTimings}ms" }

        // println("Close...")
        logger.info { "Finished processing all images! Closing channel..." }
        channel.close()

        logger.info { "Waiting for the channel job to finish..." }
        // Wait for all images to be sent to ffmpeg
        job.join()

        logger.info { "Time spent writing pixels images: ${writingTimings}ms" }

        logger.info { "Closing ffmpeg output stream..." }
        processBuilder.outputStream.close()

        logger.info { "Waiting for ffmpeg..." }
        processBuilder.waitFor()

        return GeneratorsUtils.readBytesAndDelete(outputFile)
            .also {
                File("./test-output.mp4")
                    .writeBytes(it)
            }
    }

    private fun getCharacterIndexCandidateForChavesHat(text: String): Int {
        var indexOfTheChavesHat: Int? = null

        // First we are going to try finding based on "characters that fit the hat pretty nicely"
        for ((index, char) in text.withIndex()) {
            if (char in GOOD_CANDIATES_FOR_CHAVES_HAT) {
                indexOfTheChavesHat = index
                break
            }
        }

        // If not, the first lowercase will be selected
        if (indexOfTheChavesHat == null) {
            for ((index, char) in text.withIndex()) {
                if (char.isLowerCase()) {
                    indexOfTheChavesHat = index
                    break
                }
            }
        }

        // If all else fails, select a random character
        if (indexOfTheChavesHat == null)
            indexOfTheChavesHat = text.indices.random()

        return indexOfTheChavesHat
    }

    private fun findAppropriateFont(text: String): Font {
        // We will calculate the size of the hat and scale it
        var logoFontSize = 211f

        while (true) {
            val carterOneRegular = carterOneRegularBase.deriveFont(logoFontSize)

            val pixelBoundsText = getPixelBoundsForText(carterOneRegular, text)

            if (CANVAS_WIDTH > pixelBoundsText.width)
                break

            // Okay we give up, we will use 50f as the font size
            if (50f >= logoFontSize) {
                logoFontSize = 50f
                break
            }

            // Too big, let's try again with a smaller font!
            logoFontSize -= 5f
        }

        return carterOneRegularBase.deriveFont(logoFontSize)
    }

    // This is the most "correct" way to find the character height
    // Because using getStringBounds, getLineMetrics... never returned proper height
    private fun getPixelBoundsForText(font: Font, text: String) = font.createGlyphVector(FONT_RENDER_CONTEXT, text)
        .getPixelBounds(FONT_RENDER_CONTEXT, 0.0f, 0.0f)

    private fun createLogo(
        text: String,
        pixelBoundsOfTheFullText: Rectangle,
        font: Font
    ): BufferedImage {
        val logoOutline = 1
        val logo3dEffect = 25

        val chavesLogo = BufferedImage(
            pixelBoundsOfTheFullText.width + CHAVES_LOGO_PADDING,
            pixelBoundsOfTheFullText.height + CHAVES_LOGO_PADDING,
            BufferedImage.TYPE_INT_ARGB
        )

        val chavesLogoGraphics = chavesLogo.createGraphics()
        chavesLogoGraphics.enableFontAntialiasing()
        chavesLogoGraphics.font = font
        chavesLogoGraphics.color = Color(231, 27, 7)

        for (x in -logoOutline..logoOutline) {
            for (y in -logoOutline..logoOutline) {
                chavesLogoGraphics.drawString(text, x, pixelBoundsOfTheFullText.height + y)
            }
        }

        val gp = GradientPaint(
            0.0f, 0.0f,
            Color(255, 6, 0),
            0.0f, chavesLogo.height.toFloat(),
            Color(255, 255, 153)
        )

        chavesLogoGraphics.paint = gp
        chavesLogoGraphics.drawString(text, 0, pixelBoundsOfTheFullText.height)

        val newBase = BufferedImage(chavesLogo.width, chavesLogo.height, BufferedImage.TYPE_INT_ARGB)
        val nbGraphics = newBase.createGraphics()

        for (it in logo3dEffect downTo 1) {
            val smush = chavesLogo.getScaledInstance(
                chavesLogo.width - (it * 2),
                chavesLogo.height,
                BufferedImage.SCALE_SMOOTH
            ).toBufferedImage()

            for (x in 0 until smush.width) {
                for (y in 0 until smush.height) {
                    val color = Color(smush.getRGB(x, y), true)
                    val hsb = Color.RGBtoHSB(color.red, color.green, color.blue, null)

                    val newColor = Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2] - (it * 0.01f)))

                    smush.setRGB(
                        x,
                        y,
                        Color(
                            newColor.red,
                            newColor.green,
                            newColor.blue,
                            color.alpha
                        ).rgb
                    )
                }
            }

            nbGraphics.drawImage(
                smush,
                it,
                0,
                null
            )
        }

        nbGraphics.drawImage(
            chavesLogo,
            0,
            0,
            null
        )

        return newBase
    }
}