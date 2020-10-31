package net.perfectdreams.imageserver.utils

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import java.io.File
import java.net.URL
import java.util.*

fun main() {
    val http = HttpClient {}

    runBlocking {
        val r = http.post<HttpResponse>("https://gabriela-canary.loritta.website/api/v1/videos/carly-aaah") {
            body = buildJsonObject {
                putJsonArray("images") {
                    addJsonObject {
                        put("type", "url")
                        put("content", "https://cdn.discordapp.com/emojis/395010059157110785.png?v=1")
                    }
                    addJsonObject {
                        put("type", "url")
                        put("content", "https://cdn.discordapp.com/emojis/741055999430885437.png?v=1")
                    }
                }
            }.toString()
        }

        println(r)

        File("C:\\Users\\Leonardo\\Documents\\LorittaAssets\\GabrielaImageGen\\temp\\carly-aaah.mp4")
            .writeBytes(r.readBytes())
    }
}