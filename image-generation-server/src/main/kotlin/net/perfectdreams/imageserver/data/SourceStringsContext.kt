package net.perfectdreams.imageserver.data

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.*
import java.lang.RuntimeException
import java.net.URL
import java.util.*
import javax.imageio.ImageIO

class SourceStringsContext(val strings: List<SourceStringData>) {
    companion object {
        fun from(input: String): SourceStringsContext {
            val result = Json.parseToJsonElement(input)
                    .jsonObject

            if (!result.containsKey("strings"))
                throw RuntimeException("Strings Array not found!")

            val images = result["strings"]!!.jsonArray

            return SourceStringsContext(
                    Json.decodeFromJsonElement(ListSerializer(SourceStringData.serializer()), images)
            )
        }
    }

    fun retrieveStringOrNull(index: Int) = strings.getOrNull(index)?.string

    fun retrieveString(index: Int): String {
        return retrieveStringOrNull(index) ?: throw IllegalArgumentException("Missing string!")
    }
}