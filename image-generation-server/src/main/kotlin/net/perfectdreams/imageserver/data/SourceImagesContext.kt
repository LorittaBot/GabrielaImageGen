package net.perfectdreams.imageserver.data

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import net.perfectdreams.imageserver.utils.ConnectionManager
import net.perfectdreams.imageserver.utils.ImageUtils
import net.perfectdreams.imageserver.utils.UntrustedURLException
import java.awt.image.BufferedImage
import java.util.*
import javax.imageio.ImageIO

class SourceImagesContext(val connectionManager: ConnectionManager, val images: List<SourceImageData>) {
    companion object {
        fun from(connectionManager: ConnectionManager, input: String) = fromOrNull(connectionManager, input) ?: throw RuntimeException("Image Array not found!")

        fun fromOrNull(connectionManager: ConnectionManager, input: String): SourceImagesContext? {
            val result = Json.parseToJsonElement(input)
                .jsonObject

            if (!result.containsKey("images"))
                return null

            val images = result["images"]!!.jsonArray

            return SourceImagesContext(
                connectionManager,
                Json.decodeFromJsonElement(ListSerializer(SourceImageData.serializer()), images)
            )
        }
    }

    suspend fun retrieveImage(index: Int): BufferedImage {
        val imageData = images.getOrNull(index) ?: throw IllegalArgumentException("Missing image!")

        return when (imageData.type) {
            SourceImageDataType.URL -> {
                if (!connectionManager.isTrusted(imageData.content))
                    throw UntrustedURLException(imageData.content)

                ImageUtils.downloadImage(imageData.content) ?: throw IllegalArgumentException("Invalid image provided")
            }

            SourceImageDataType.BASE64 -> {
                ImageIO.read(Base64.getDecoder().decode(imageData.content).inputStream())
            }
        }
    }
}