package net.perfectdreams.imageserver.data

import kotlinx.serialization.json.Json

fun main() {
    val img = SourceImageData(
            SourceImageDataType.URL,
            "https://google.com"
    )

    println(Json.encodeToString(SourceImageData.serializer(), img))
}