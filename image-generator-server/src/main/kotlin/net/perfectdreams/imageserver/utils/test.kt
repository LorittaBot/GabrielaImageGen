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
    val http = HttpClient {

    }

    runBlocking {
        val r = http.post<HttpResponse>("http://127.0.0.1:8001/api/v1/videos/attack-on-heart") {
            body = buildJsonObject {
                putJsonArray("images") {
                    addJsonObject {
                        put("type", "url")
                        put("content", "https://cdn.discordapp.com/emojis/793233708085805066.png?v=1")
                    }
                }
            }.toString()
        }

        println(r.status)
        println(r)

        File("L:\\LorittaAssets\\GabrielaImageGen\\temp\\attack_on_heart.mp4")
            .writeBytes(r.readBytes())
    }
}