package net.perfectdreams.imageserver.utils

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import java.io.File
import java.util.*

fun main() {
    val http = HttpClient(Apache) {
        this.engine {
            this.connectTimeout = 60_000
            this.connectionRequestTimeout = 60_000
            this.socketTimeout = 60_000
        }
    }

    runBlocking {
        if (true) {
            val r = http.post<HttpResponse>("http://127.0.0.1:8001/api/v1/images/meme-maker") {
                body = buildJsonObject {
                    putJsonArray("images") {
                        addJsonObject {
                            put("type", "url")
                            put("content", "https://cdn.discordapp.com/emojis/523233744656662548.png?v=1")
                        }
                    }

                    putJsonArray("strings") {
                        addJsonObject {
                            put("string", "power depois que a lori")
                        }
                        addJsonObject {
                            put("string", "chega em uma nova milestone")
                        }
                    }
                }.toString()
            }

            println(r.status)
            println(r)

            File("L:\\LorittaAssets\\GabrielaImageGen\\temp\\meme_maker.png")
                .writeBytes(r.readBytes())
            return@runBlocking
        }
        val r = http.post<HttpResponse>("http://127.0.0.1:8001/api/v1/images/nichijou-yuuko-paper") {
            body = buildJsonObject {
                putJsonArray("images") {
                    addJsonObject {
                        put("type", "url")
                        put("content", "https://cdn.discordapp.com/emojis/585536267530534913.png?v=1")
                    }
                }
            }.toString()
        }

        println(r.status)
        println(r)

        File("L:\\LorittaAssets\\GabrielaImageGen\\temp\\mania_title_card.png")
            .writeBytes(r.readBytes())
    }
}