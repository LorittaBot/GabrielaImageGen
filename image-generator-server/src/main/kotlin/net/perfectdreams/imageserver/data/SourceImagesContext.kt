package net.perfectdreams.imageserver.data

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.*
import java.awt.image.BufferedImage
import java.lang.RuntimeException
import java.net.URL
import java.util.*
import javax.imageio.ImageIO

class SourceImagesContext(val images: List<SourceImageData>) {
    companion object {
        fun from(input: String): SourceImagesContext {
            val result = Json.parseToJsonElement(input)
                    .jsonObject

            if (!result.containsKey("images"))
                throw RuntimeException("Image Array not found!")

            val images = result["images"]!!.jsonArray

            return SourceImagesContext(
                    Json.decodeFromJsonElement(ListSerializer(SourceImageData.serializer()), images)
            )
        }
    }

    suspend fun retrieveImage(index: Int): BufferedImage {
        val imageData = images.getOrNull(index) ?: throw IllegalArgumentException("Missing image!")

        return when (imageData.type) {
            SourceImageDataType.URL -> {
                ImageIO.read(URL(imageData.content).openConnection().getInputStream())
            }

            SourceImageDataType.BASE64 -> {
                ImageIO.read(Base64.getDecoder().decode(imageData.content).inputStream())
            }
        }
    }
}