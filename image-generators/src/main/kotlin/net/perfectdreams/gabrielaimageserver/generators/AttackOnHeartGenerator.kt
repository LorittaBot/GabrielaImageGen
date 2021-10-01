package net.perfectdreams.gabrielaimageserver.generators

import net.perfectdreams.gabrielaimageserver.generators.utils.GeneratorsUtils
import net.perfectdreams.gabrielaimageserver.generators.utils.toBufferedImage
import net.perfectdreams.gabrielaimageserver.graphics.LorittaImage
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.concurrent.thread

class AttackOnHeartGenerator(
    val tempFolder: File,
    val assetsFolder: File,
    val ffmpegPath: File
) : SingleSourceImageToByteArrayGenerator {
    override fun generate(source: BufferedImage): ByteArray {
        val outputFileName = GeneratorsUtils.generateFileName("attack-on-heart", "mp4")
        val outputFile = File(tempFolder, outputFileName)

        val imageFileName = GeneratorsUtils.generateFileName("attack-on-heart-target-image", "png")
        val imageFile = File(tempFolder, imageFileName)

        // we only need to scale it once, no need to scale every time inside of the "repeat" block
        val lorittaImage = LorittaImage(
            source.getScaledInstance(960, 540, BufferedImage.SCALE_SMOOTH)
                .toBufferedImage()
        )

        lorittaImage.setCorners(
            203f, 0f,
            893f, 0f,
            857f, 539f,
            213f , 539f
        )

        println("Writing image... $imageFile")

        ImageIO.write(
            lorittaImage.bufferedImage,
            "png",
            imageFile
        )

        // First we are going to store the new video in memory (using libvpx-vp9)
        val processBuilder = ProcessBuilder(
            ffmpegPath.toString(),
            "-c:v",
            "libvpx-vp9",
            "-i",
            File(assetsFolder, "attack_on_heart.webm").toString(),
            "-i",
            imageFile.toString(),
            "-filter_complex",
            "[1:v][0:v] overlay=1:1:enable='between(t,0,7)', fade=in:0:20",
            "-c:a",
            "aac",
            "-vcodec",
            "libx264",
            "-preset",
            "superfast",
            "-acodec",
            "aac",
            "-y",
            outputFile.toString(),
        ).also { println(it.command().joinToString(" ")) }.redirectErrorStream(true)
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

        println("Waiting for ffmpeg...")
        processBuilder.waitFor()

        println("Finished!")

        val result = outputFile.readBytes()
        outputFile.delete()
        imageFile.delete()

        return result
    }
}