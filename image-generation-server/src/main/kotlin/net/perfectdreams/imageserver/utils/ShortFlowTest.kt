package net.perfectdreams.imageserver.utils

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.Executors
import kotlin.concurrent.thread
import kotlin.math.absoluteValue

val ONE_SECOND_LENGTH = 88200

fun main() {
    val rawAudioFile = File("L:\\Tools\\ffmpeg\\yoda_rezende\\audio_full.raw")

    val inputStream = rawAudioFile.inputStream()

    var frameIndex = 0

    var offset = 0

    println("Doing something...")

    // File("L:\\Tools\\ffmpeg\\yoda_rezende\\no_silent.raw").delete()

    val frames = mutableListOf<Int>()

    while (true) {
        // Every second (mono) is 88200 bytes because the sample rate is 44.1Hz
        // And because it is 16 bits (so 2 bits)
        val array = ByteArray(ONE_SECOND_LENGTH / 30)

        val frame = inputStream.read(array)

        if (frame == -1)
            break

        /* if (frameIndex >= 50_000)
            break */

        // println("Yay!")

        val byteBuffer = ByteBuffer.wrap(array)
                .order(ByteOrder.LITTLE_ENDIAN)

        val list = mutableListOf<Int>()

        while (byteBuffer.hasRemaining())
            list.add(byteBuffer.short.toInt())

        val value = list.firstOrNull { it > 32767 }

        if (value != null)
            throw RuntimeException("Value out of bounds! $value")

        val value2 = list.firstOrNull { -327678 > it }

        if (value2 != null)
            throw RuntimeException("Value out of bounds #2! $value2")

        val stuff = list.sumBy { it.absoluteValue }
        val avg2 = quadraticMean(list.map { it.toDouble() }.toTypedArray())
        val avg = list.average()

        if (600 >= avg2) {
            /* File("L:\\Tools\\ffmpeg\\yoda_rezende\\no_silent.raw")
                    .appendBytes(array) */
            frames.add(frameIndex + 1)
        }

        println("Average of $frameIndex -> $stuff ($avg) ($avg2)")

        if (array.all { it == 0.toByte() }) {
            println("Second at $frameIndex is fully silent!")
        }

        offset += ONE_SECOND_LENGTH / 30
        frameIndex++
    }

    if (true) {
        var currentFrameIndex = 1
        println("Frames that should be loaded: ${frames.size}")

        val jobs = mutableListOf<Job>()

        val executors = Executors.newFixedThreadPool(6)
                .asCoroutineDispatcher()

        val notGeneratedYetFrames = frames.filter {
            !File("L:\\Tools\\ffmpeg\\flow_mudo_frames\\temp\\frame_${(it).toString().padStart(9, '0')}.jpg")
                    .exists()
        }

        println("Missing frames: $notGeneratedYetFrames")

        for (framesChunked in notGeneratedYetFrames.chunked(1)) {
            if (true || 1 in framesChunked) {
                jobs += GlobalScope.launch(executors) {
                    val startFrame = framesChunked.first()
                    val endFrame = framesChunked.last()
                    var paddedStartFrame = (startFrame - 35)
                    var paddedEndFrame = (endFrame + 35)

                    val build = (paddedStartFrame..paddedEndFrame).joinToString("+") {
                        var frameRelative = Math.max(it - Math.abs(paddedStartFrame), 0)
                        println("Frame: $frameRelative")

                        "eq(n\\,${frameRelative})"
                    }
                    println(build)

                    println("Extracting ${framesChunked.joinToString(", ")}")

                    fun buildFfmpegStuff(start: Int): String {
                        val hours = Math.max(0, start / 3600)
                        val minutes = Math.max((start % 3600) / 60, 0)
                        val seconds = Math.max(start % 60, 0)

                        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
                    }

                    println("Start Frame: $paddedStartFrame")
                    println("End Frame: $paddedEndFrame")
                    val startSeek = buildFfmpegStuff((paddedStartFrame / 29.97).toInt())
                    val endSeek = buildFfmpegStuff(((paddedEndFrame) / 29.97).toInt())

                    println("Starting Seek At: $startSeek")
                    println("Ending Seek At: $endSeek")

                    // File("L:\\Tools\\ffmpeg\\flow_mudo_frames\\temp\\").listFiles().forEach { it.delete() }

                    try {
                        val processBuilder = ProcessBuilder(
                                "L:\\Tools\\ffmpeg\\ffmpeg.exe",
                                "-ss",
                                startSeek,
                                "-to",
                                endSeek,
                                "-copyts",
                                "-i",
                                "L:\\Tools\\ffmpeg\\YODA - Flow Podcast #297-08nTRrrQY14.mp4",
                                "-vf",
                                "select='$build'",
                                "-frame_pts",
                                "true",
                                "L:\\Tools\\ffmpeg\\flow_mudo_frames\\temp\\frame_%09d.jpg"
                        ).redirectErrorStream(true)
                                .inheritIO()
                                .start()
                                .waitFor()
                    } catch (e: Exception) {
                        println("FRAMES THAT ARE TOO BIG: $framesChunked $build")
                    }

                    println("Done! $framesChunked")
                }
            }
        }

        runBlocking {
            jobs.joinAll()
        }

        println("Copying Files...")
        for (frame in frames) {
            // println("Copying Frame $frame -> $currentFrameIndex")
            val sourceFile = File("L:\\Tools\\ffmpeg\\flow_mudo_frames\\temp\\frame_${(frame).toString().padStart(9, '0')}.jpg")

            if (sourceFile.exists()) {
                val outputFile = File("L:\\Tools\\ffmpeg\\flow_mudo_frames\\copied\\frame_${(currentFrameIndex).toString().padStart(9, '0')}.jpg")

                // Avoid taking too long to finish the copy files process
                if (!outputFile.exists())
                    sourceFile
                            .copyTo(File("L:\\Tools\\ffmpeg\\flow_mudo_frames\\copied\\frame_${(currentFrameIndex).toString().padStart(9, '0')}.jpg"), true)

                currentFrameIndex++
            } else {
                println("DOES NOT EXIST!!! $sourceFile")
                continue
            }
        }

        println("Finished Copying Files!")
    }

    println("Finished! Seconds: $frameIndex")

    val processBuilder = ProcessBuilder(
            "L:\\Tools\\ffmpeg\\ffmpeg.exe",
            "-framerate",
            "29.97",
            "-i",
            "L:\\Tools\\ffmpeg\\flow_mudo_frames\\copied\\frame_%09d.jpg",
            "-f",
            "s16le",
            "-acodec",
            "pcm_s16le",
            "-ar",
            "44100",
            "-ac",
            "1",
            "-i",
            "L:\\Tools\\ffmpeg\\yoda_rezende\\no_silent.raw",
            "-c:v",
            "libx264",
            "-preset",
            "superfast",
            "-pix_fmt",
            "yuv420p",
            "-y",
            // Due to the way MP4 containers work (it goes back after writing all data!), we need to write directly to a file
            "L:\\Tools\\ffmpeg\\flow_mudo_frames\\end_result.mp4"
    ).redirectErrorStream(true)
            .start()

    println("Copied!")

    thread {
        while (true) {
            val r = processBuilder.inputStream.read()
            if (r == -1)
            // Keep reading until end of input
                return@thread

            print(r.toChar())
        }
    }

    processBuilder.outputStream.close()

    println("Waiting for ffmpeg...")
    processBuilder.waitFor()

    println("Finished!")
}

fun quadraticMean(vector: Array<Double>) : Double {
    val sum = vector.sumByDouble { it * it }
    return Math.sqrt(sum / vector.size)
}