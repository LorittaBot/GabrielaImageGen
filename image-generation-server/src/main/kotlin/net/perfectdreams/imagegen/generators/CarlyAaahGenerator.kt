package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.graphics.LorittaImage
import net.perfectdreams.imageserver.generators.Generators
import net.perfectdreams.imageserver.utils.extensions.toBufferedImage
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.concurrent.thread

class CarlyAaahGenerator(
        val tempFolder: File,
        val assetsFolder: File,
        val ffmpegPath: File
) : SingleSourceBufferedImageToByteArrayGenerator {
    override fun generate(source: BufferedImage): ByteArray {
        val outputFileName = Generators.generateFileName("carly-aaah", "mp4")
        val outputFile = File(tempFolder, outputFileName)

        println("Output file: $outputFile")

        val processBuilder = ProcessBuilder(
                ffmpegPath.toString(),
                "-framerate",
                "24",
                "-i",
                "-", // We will write to output stream
                "-i",
                File(assetsFolder, "output-audio.aac").toString(),
                "-vcodec",
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

        // we only need to scale it once, no need to scale every time inside of the "repeat" block
        val lorittaImage = LorittaImage(
                source.getScaledInstance(1280, 720, BufferedImage.SCALE_SMOOTH)
                        .toBufferedImage()
        )

        lorittaImage.setCorners(
                340f, 200f,
                695f, 198f,
                717f, 606f,
                366f, 611f
        )

        repeat(103) {
            val pad = (it + 1).toString().padStart(3, '0')

            val input = File(assetsFolder, "${pad}.jpg")

            if (it in 57..64) {
                val image = ImageIO.read(input)
                val graphics = image.createGraphics()

                graphics.drawImage(lorittaImage.bufferedImage, 0, 0, null)

                val noCopy = ByteArrayOutputStream()
                ImageIO.write(image, "jpg", noCopy)
                processBuilder.outputStream.write(noCopy.toByteArray())
            } else {
                processBuilder.outputStream.write(input.readBytes())
            }

            println("Flush...")
            processBuilder.outputStream.flush()
        }

        println("Close...")
        processBuilder.outputStream.close()

        println("Waiting for ffmpeg...")
        processBuilder.waitFor()

        println("Finished!")

        return Generators.readBytesAndDelete(outputFile)
    }
}