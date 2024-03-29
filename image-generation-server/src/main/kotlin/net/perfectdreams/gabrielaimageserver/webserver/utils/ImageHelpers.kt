package net.perfectdreams.gabrielaimageserver.webserver.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.perfectdreams.gabrielaimageserver.data.Base64ImageData
import net.perfectdreams.gabrielaimageserver.data.SourceImageData
import net.perfectdreams.gabrielaimageserver.data.URLImageData
import net.perfectdreams.gabrielaimageserver.exceptions.ImageNotFoundException
import net.perfectdreams.gabrielaimageserver.exceptions.UntrustedURLException
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO

suspend fun SourceImageData.retrieveImage(connectionManager: ConnectionManager) = withContext(Dispatchers.IO) {
    when (this@retrieveImage) {
        is Base64ImageData -> {
            ImageIO.read(Base64.getDecoder().decode(this@retrieveImage.data).inputStream())
                ?: throw ImageNotFoundException()
        }
        is URLImageData -> {
            val url = this@retrieveImage.url

            if (!connectionManager.isTrusted(url))
                throw UntrustedURLException(url)

            try {
                ImageUtils.downloadImage(url) ?: throw ImageNotFoundException()
            } catch (e: FileNotFoundException) {
                // This can be thrown when calling "getHeaderFieldInt"
                throw ImageNotFoundException()
            } catch (e: IOException) {
                // This can be thrown when trying to read the image header with SimpleImageInfo
                throw ImageNotFoundException()
            }
        }
    }
}