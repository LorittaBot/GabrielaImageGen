package net.perfectdreams.imageserver.utils

import mu.KotlinLogging
import java.io.File
import kotlin.concurrent.thread

class Gifsicle(val binaryPath: File) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    fun optimizeGIF(input: ByteArray, lossy: Int = 200): ByteArray {
        logger.info { "Optimizing GIF in Gifsicle! Input has ${input.size} bytes!!" }
        val processBuilder = ProcessBuilder(
            binaryPath.toString(),
            "-w", // ignore warnings
            // "--careful", // avoids discord bugs
            "-O3",
            // "--lossy=$lossy",
            // "--colors",
            // "256"
        ).redirectErrorStream(true)

        val process = processBuilder.start()

        val outputStream = process.outputStream

        logger.info { "Writing bytes..." }
        outputStream.write(input)
        logger.info { "Flushing bytes..." }
        outputStream.flush()
        logger.info { "Closing output stream..." }
        outputStream.close()

        // If we don't read it here, Gifsicle gets stuck for some reason
        val bytes = process.inputStream.readAllBytes()

        logger.info { "Waiting for Gifsicle..." }
        process.waitFor()

        logger.info { "Gifsicle finished!" }
        return bytes
    }
}