package net.perfectdreams.gabrielaimageserver.webserver.utils

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import net.perfectdreams.gabrielaimageserver.data.GigaChadRequest
import java.io.File

fun main() {
    val http = HttpClient(Apache) {
        install(HttpTimeout) {
            this.requestTimeoutMillis = 999999
            this.requestTimeoutMillis = 999999
            this.socketTimeoutMillis = 999999
        }
    }

    runBlocking {
        if (true) {
            val r = http.post<HttpResponse>("http://127.0.0.1:8001/api/v2/videos/gigachad") {
                body = Json.encodeToString(
                    GigaChadRequest(
                        // "mmmm mmmm mmmm mmmm mmmm mmmm mmmm mmmm mmmm mmmm mmmm mmmm ",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce dictum elit a nisi commodo suscipit. Cras faucibus id nisl eget consequat. Proin pulvinar volutpat pretium. Mauris dapibus lectus a mauris interdum vestibulum. Donec vitae porta elit, et iaculis enim. Mauris id feugiat felis, sit amet blandit orci. Sed id nibh at enim molestie vehicula. Vivamus nec purus sit amet mauris suscipit fermentum. Nullam molestie lacinia eros quis eleifend. Aliquam posuere sagittis bibendum. Proin rutrum bibendum lacus, a mollis ipsum gravida eget. Maecenas eget lacinia erat, ac efficitur turpis. In ullamcorper quam sit amet elit rhoncus, in venenatis nisi commodo.",
                        // "mee6 fans tentando justificar que é certo o mee6 vender nfts",
                        "loritta fans"
                    )
                )
            }

            println(r.status)
            println(r)

            File("L:\\LorittaAssets\\GabrielaImageGen\\temp\\gigachad.mp4")
                .writeBytes(r.readBytes())
            return@runBlocking
        }

        println(
            buildJsonObject {
                putJsonArray("images") {
                    addJsonObject {
                        put("type", "url")
                        put("content", "https://cdn.discordapp.com/emojis/585536267530534913.png?v=1")
                    }
                }
            }.toString()
        )

        return@runBlocking

        val r = http.post<HttpResponse>("https://gabriela.loritta.website/api/v1/videos/cocielo-chaves") {
            userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0")

            body = buildJsonObject {
                putJsonArray("images") {
                    addJsonObject {
                        put("type", "url")
                        put("content", "https://cdn.discordapp.com/emojis/585536267530534913.png?v=1")
                    }
                    addJsonObject {
                        put("type", "url")
                        put("content", "https://cdn.discordapp.com/emojis/585536267530534913.png?v=1")
                    }
                    addJsonObject {
                        put("type", "url")
                        put("content", "https://cdn.discordapp.com/emojis/585536267530534913.png?v=1")
                    }
                    addJsonObject {
                        put("type", "url")
                        put("content", "https://cdn.discordapp.com/emojis/585536267530534913.png?v=1")
                    }
                    addJsonObject {
                        put("type", "url")
                        put("content", "https://cdn.discordapp.com/emojis/585536267530534913.png?v=1")
                    }
                }
            }.toString()
        }

        println(r.status)
        println(r)

        File("L:\\LorittaAssets\\GabrielaImageGen\\temp\\cocielo-chaves.mp4")
            .writeBytes(r.readBytes())
    }
}