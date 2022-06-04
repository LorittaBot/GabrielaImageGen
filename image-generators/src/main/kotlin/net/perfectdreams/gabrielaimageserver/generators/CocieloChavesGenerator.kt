package net.perfectdreams.gabrielaimageserver.generators

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.perfectdreams.gabrielaimageserver.generators.utils.GeneratorsUtils
import net.perfectdreams.gabrielaimageserver.graphics.LorittaImage
import java.awt.AlphaComposite
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.io.File
import javax.imageio.ImageIO
import kotlin.concurrent.thread

class CocieloChavesGenerator(
    val tempFolder: File,
    val assetsFolder: File,
    val ffmpegPath: File
) : Generator {
    companion object {
        /**
         * Loads keyframes from a CSV file generated with the "Extract Multiple Selected Motion Data With Frames.cs" file
         *
         * Script URL: https://gist.github.com/MrPowerGamerBR/e5147231bb560028fd0a79d2f5f3b749
         *
         * @param file the keyframes csv file
         * @return a list containing all the keyframes
         */
        private fun readKeyframes(file: File): Map<Int, KeyframeInfo> {
            val keyframesLines = file
                .readLines()

            val keyframesCocielo = mutableMapOf<Int, KeyframeInfo>()

            keyframesLines.forEach {
                val split = it.split(";")
                val frame = split[0]
                val locationX = split[1]
                val locationY = split[2]
                val angle = split[3]
                val scaleX = split[4]
                val scaleY = split[5]
                val shear = split[6]
                val mode = split[7]

                keyframesCocielo[frame.toInt()] = KeyframeInfo(
                    frame.toInt(),
                    locationX.replace(",", ".").toDouble(),
                    locationY.replace(",", ".").toDouble(),
                    angle.replace(",", ".").toDouble(),
                    scaleX.replace(",", ".").toDouble(),
                    scaleY.replace(",", ".").toDouble(),
                    shear.replace(",", ".").toDouble(),
                    mode
                )
            }

            return keyframesCocielo
        }

        private val NO_EDITS_FRAME_RANGES = listOf(
            0..56,
            777..794,
            813..832,
            952..990
        )
    }

    // Preload Keyframes
    private val keyframesCocielo = readKeyframes(
        File(assetsFolder, "keyframes_cocielo.csv")
    )

    private val keyframesIgao = readKeyframes(
        File(assetsFolder, "keyframes_igao.csv")
    )

    private val keyframesFriend1 = readKeyframes(
        File(assetsFolder, "keyframes_friend1.csv")
    )

    private val keyframesFriend2 = readKeyframes(
        File(assetsFolder, "keyframes_friend2.csv")
    )

    private val keyframesFriend3 = readKeyframes(
        File(assetsFolder, "keyframes_friend3.csv")
    )

    /**
     * Stretches the [image] to the video frame size (currently 640x360)
     *
     * The image will be stretched with the [BufferedImage.SCALE_FAST] algorithm
     *
     * @param image the image that will be stretched
     * @return the stretched image
     */
    private fun stretchToFrameSize(image: BufferedImage): BufferedImage {
        val imageStretched = BufferedImage(640, 360, BufferedImage.TYPE_INT_ARGB)
        imageStretched.createGraphics().drawImage(image.getScaledInstance(640, 360, BufferedImage.SCALE_FAST), 0, 0, null)
        return imageStretched
    }

    private val noEditsFramesData = mutableMapOf<Int, ByteArray>()
    private val overlaysData = mutableMapOf<String, Pair<Int, Int>>()

    init {
        // Read no edit frames into memory
        for (frameRange in NO_EDITS_FRAME_RANGES) {
            for (frame in frameRange) {
                val paddedFrames = frame.toString()
                    .padStart(6, '0')

                val fileName = "chaves_$paddedFrames.jpeg"

                val imageFrameFile = File(assetsFolder, "frames/$fileName")

                noEditsFramesData[frame] = (ImageIO.read(imageFrameFile).raster.dataBuffer as DataBufferByte).data
            }
        }

        // Read overlay data
        File(assetsFolder, "overlay_data.csv").readLines().forEach {
            val (fileName, startX, startY) = it.split(";")
            overlaysData[fileName] = Pair(startX.toInt(), startY.toInt())
        }
    }

    suspend fun generate(
        cocieloTargetImage: BufferedImage,
        igaoTargetImage: BufferedImage,
        friend1TargetImage: BufferedImage,
        friend2TargetImage: BufferedImage,
        friend3TargetImage: BufferedImage,
        decreasedFrameRate: Boolean
    ): ByteArray {
        // Prescale the templates
        val templateCocieloPrescaled = stretchToFrameSize(cocieloTargetImage)
        val templateIgaoPrescaled = stretchToFrameSize(igaoTargetImage)
        val templateFriend1Prescaled = stretchToFrameSize(friend1TargetImage)
        val templateFriend2Prescaled = stretchToFrameSize(friend2TargetImage)
        val templateFriend3Prescaled = stretchToFrameSize(friend3TargetImage)

        val outputFileName = GeneratorsUtils.generateFileName("cocielo-chaves", "mp4")
        val outputFile = File(tempFolder, outputFileName)

        val processBuilder = ProcessBuilder(
            ffmpegPath.toString(),
            "-framerate",
            if (decreasedFrameRate)
                "15"
            else
                "30",
            "-f",
            "rawvideo",
            "-pixel_format",
            "bgr24", // This is what the "BufferedImage.TYPE_3BYTE_BGR" uses behind the scenes
            "-video_size",
            "640x360",
            "-i",
            "-", // We will write to output stream
            "-i",
            File(assetsFolder, "chaves.ogg").toString(),
            "-c:v",
            "libx264",
            "-c:a",
            "copy",
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

        val frameRange = if (decreasedFrameRate)
            (0..990 step 2)
        else
            (0..990)

        // Generate frames!
        val stuff = GlobalScope.launch {
            val frameJobs = mutableMapOf<Int, Deferred<ByteArray>>()

            frameRange.map { frame ->
                frameJobs[frame] = GlobalScope.async(start = CoroutineStart.LAZY) {
                    // We are in a "No Edit Frame", so we are going to just copy the frame data as is instead of loading it into memory using ImageIO (and that uses a lot of memory!)
                    val noEditFrameData = noEditsFramesData[frame]
                    if (noEditFrameData != null) {
                        // println("[Frame $frame] Using No Edit Frame Data")
                        return@async noEditFrameData
                    }

                    var time = System.currentTimeMillis()
                    val paddedFrames = frame.toString()
                        .padStart(6, '0')

                    val fileName = "chaves_$paddedFrames.jpeg"

                    val imageFrameFile = File(assetsFolder, "frames/$fileName")

                    val imageFrame = withContext(Dispatchers.IO) {
                        ImageIO.read(imageFrameFile)
                    }
                    // println("[Frame $frame] Took ${System.currentTimeMillis() - time}ms to load the frame")

                    // println("[Frame $frame] Took ${System.currentTimeMillis() - start}ms to load the frame into memory!")

                    // time = System.currentTimeMillis()
                    // Apply the graphics from the keyframes
                    val imageFrameGraphics = imageFrame.createGraphics()
                    if (frame in 49..80) {
                        // Frames 49..80 are a fade in, so let's apply a alpha composite to the graphics!
                        imageFrameGraphics.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (frame - 49) / (49.toFloat()))
                    }
                    // println("[Frame $frame] Took ${System.currentTimeMillis() - time}ms to create and apply alpha composite")
                    // time = System.currentTimeMillis()

                    applyGraphicsFromKeyframes(
                        frame,
                        imageFrame,
                        imageFrameGraphics,
                        templateCocieloPrescaled,
                        keyframesCocielo
                    )

                    applyGraphicsFromKeyframes(
                        frame,
                        imageFrame,
                        imageFrameGraphics,
                        templateIgaoPrescaled,
                        keyframesIgao
                    )

                    applyGraphicsFromKeyframes(
                        frame,
                        imageFrame,
                        imageFrameGraphics,
                        templateFriend1Prescaled,
                        keyframesFriend1
                    )

                    applyGraphicsFromKeyframes(
                        frame,
                        imageFrame,
                        imageFrameGraphics,
                        templateFriend2Prescaled,
                        keyframesFriend2
                    )

                    applyGraphicsFromKeyframes(
                        frame,
                        imageFrame,
                        imageFrameGraphics,
                        templateFriend3Prescaled,
                        keyframesFriend3
                    )
                    // println("[Frame $frame] Took ${System.currentTimeMillis() - time}ms to apply the keyframes")
                    // time = System.currentTimeMillis()

                    // println("[Frame $frame] Took ${System.currentTimeMillis() - start}ms to apply the keyframes!")

                    val overlayData = overlaysData[fileName.replace("chaves", "overlay").replace("jpeg", "png")]

                    if (overlayData != null) {
                        val overlayFrame = File(assetsFolder, "overlay/${fileName.replace("chaves", "overlay").replace("jpeg", "png")}")

                        // If a overlay frame exists, draw it on top of everything!
                        val overlay = withContext(Dispatchers.IO) { ImageIO.read(overlayFrame) }

                        imageFrame.createGraphics().drawImage(
                            overlay,
                            overlayData.first,
                            overlayData.second,
                            null
                        )
                    }
                    // println("[Frame $frame] Took ${System.currentTimeMillis() - time}ms to apply overlays!")

                    // println("[Frame $frame] Took ${System.currentTimeMillis() - start}ms to do overlay data stuff!")

                    return@async (imageFrame.raster.dataBuffer as DataBufferByte).data
                }
            }

            for (index in frameRange) {
                // Start the next 8 jobs
                ((index + 1)..Math.min(frameRange.last, index + 16)).forEach {
                    // println("Starting Job $it because $index was completed!")
                    frameJobs[it]?.start()
                }

                val job = frameJobs[index]

                val frame = job?.await()

                if (frame != null) {
                    // println("Writing frame $index")
                    // Write to ffmpeg output
                    withContext(Dispatchers.IO) {
                        processBuilder.outputStream.write(frame)
                        processBuilder.outputStream.flush()
                    }
                }

                // Remove from the job list so it can be GC'd
                frameJobs.remove(index)
            }
        }



        val now = System.currentTimeMillis()

        stuff.join()

        println("Took ${System.currentTimeMillis() - now}ms to generate the frames!")

        println("Close...")
        processBuilder.outputStream.close()

        println("Waiting for ffmpeg...")
        processBuilder.waitFor()

        println("Finished!")
        println("Total: ${System.currentTimeMillis() - now}ms")

        return GeneratorsUtils.readBytesAndDelete(outputFile)
    }

    /**
     * Renders the [alreadyScaledImage] in the [imageFrame] with a keyframe if there is a keyframe for the [frame].
     *
     * @param frame              the current frame
     * @param imageFrame         the background frame
     * @param alreadyScaledImage the already scaled target image
     * @param keyframes          a list with the keyframes
     */
    private fun applyGraphicsFromKeyframes(frame: Int, imageFrame: BufferedImage, imageFrameGraphics: Graphics2D, alreadyScaledImage: BufferedImage, keyframes: Map<Int, KeyframeInfo>) {
        val keyframe = keyframes[frame]

        if (keyframe != null) {
            // To avoid stretching the image on every loop, we are going to use an already stretched image
            val image = LorittaImage(alreadyScaledImage)

            val newImageSizeWidth: Double
            val newImageSizeHeight: Double

            if (keyframe.mode == "Fixed Shape") {
                // scale seems to be kinda wonky in Fixed Shape, so we are always going to use the scaleX
                newImageSizeWidth = imageFrame.height * keyframe.scaleX
                newImageSizeHeight = imageFrame.height * keyframe.scaleX
            } else {
                newImageSizeWidth = imageFrame.height * keyframe.scaleX
                newImageSizeHeight = imageFrame.height * keyframe.scaleY
            }

            val dividedWidth = newImageSizeWidth / 2
            val dividedHeight = newImageSizeHeight / 2

            val x = keyframe.locationX * imageFrame.width
            // vegas keyframes are inverted
            val y = (1 - keyframe.locationY) * imageFrame.height

            // println("${keyframe.locationY} = $y")

            val radians = Math.toRadians(keyframe.angle)
            // println("Angle: ${keyframe.angle}")

            val sine = Math.sin(radians)
            val cosine = Math.cos(radians)

            fun doAngleMultiplicationByAPoint(p: Double, q: Double): CocieloPoint {
                val s = sine
                val c = cosine

                val originX = p - x
                val originY = q - y

                //  rotate point
                //  float xnew = p.x * c - p.y * s;
                //  float ynew = p.x * s + p.y * c;
                val newX = (originX * c) - (originY * s)
                val newY = (originX * s) + (originY * c)

                return CocieloPoint(
                    (newX + x).toFloat(), (newY + y).toFloat()
                )
            }

            fun applyShearing(p: Double, q: Double): Double {
                val originX = p - x
                val originY = q - y

                val shear = (keyframe.shear * originX) + originY

                // We also need to shift the y coordinates down a bit, because if we don't the shearing moves the image to a incorrect position
                return shear + y
            }

            // println("AngleX: $angleX")
            // println("AngleY: $angleY")

            val cornersLLX = x - dividedWidth
            val cornersLLY = y - dividedHeight

            val cornersLRX = x + dividedWidth
            val cornersLRY = y - dividedHeight

            val cornersBRX = x + dividedWidth
            val cornersBRY = y + dividedHeight

            val cornersBLX = x - dividedWidth
            val cornersBLY = y + dividedHeight

            val cornersLLXAngle = doAngleMultiplicationByAPoint(cornersLLX, cornersLLY).x
            val cornersLLYAngle = doAngleMultiplicationByAPoint(cornersLLX, applyShearing(cornersLLX, cornersLLY)).y

            val cornersLRXAngle = doAngleMultiplicationByAPoint(cornersLRX, cornersLRY).x
            val cornersLRYAngle = doAngleMultiplicationByAPoint(cornersLRX, applyShearing(cornersLRX, cornersLRY)).y

            val cornersBRXAngle = doAngleMultiplicationByAPoint(cornersBRX, cornersBRY).x
            val cornersBRYAngle = doAngleMultiplicationByAPoint(cornersBRX, applyShearing(cornersBRX, cornersBRY)).y

            val cornersBLXAngle = doAngleMultiplicationByAPoint(cornersBLX, cornersBLY).x
            val cornersBLYAngle = doAngleMultiplicationByAPoint(cornersBLX, applyShearing(cornersBLX, cornersBLY)).y

            // println("Frame $frame")
            // println("($cornersLLX, $cornersLLY) - ($cornersLRX, $cornersLRY)")
            // println("($cornersBLX, $cornersBLY) - ($cornersBRX, $cornersBRY)")
            // println("~")
            // println("($cornersLLXAngle, $cornersLLYAngle) - ($cornersLRXAngle, $cornersLRYAngle)")
            // println("($cornersBLXAngle, $cornersBLYAngle) - ($cornersBRXAngle, $cornersBRYAngle)")
            // println("---")

            /* if (frame % 50 == 0) {
                println("Frames Completed: ${jobs.count { it.isCompleted }}")
            } */

            image.setCorners(
                cornersLLXAngle,
                cornersLLYAngle,

                cornersLRXAngle,
                cornersLRYAngle,

                cornersBRXAngle,
                cornersBRYAngle,

                cornersBLXAngle,
                cornersBLYAngle
            )

            // image.rotate(keyframe.angle)

            imageFrameGraphics.drawImage(image.bufferedImage, 0, 0, null)
        }
    }

    private data class KeyframeInfo(
        val frame: Int,
        val locationX: Double,
        val locationY: Double,
        val angle: Double,
        val scaleX: Double,
        val scaleY: Double,
        val shear: Double,
        val mode: String
    )

    private data class CocieloPoint(val x: Float, val y: Float)
}