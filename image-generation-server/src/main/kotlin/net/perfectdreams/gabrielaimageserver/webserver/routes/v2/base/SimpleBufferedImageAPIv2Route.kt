package net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base

import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

abstract class SimpleBufferedImageAPIv2Route<T>(
    path: String,
    m: GabrielaImageGen
) : SimpleAPIv2Route<T>(
    path,
    m,
    ContentType.Image.PNG
) {
    abstract suspend fun generateImage(data: T): BufferedImage

    override suspend fun generate(data: T): ByteArray {
        val result = generateImage(data)
        return withContext(Dispatchers.IO) {
            val output = ByteArrayOutputStream()
            ImageIO.write(result, "png", output)
            output.toByteArray()
        }
    }
}