package net.perfectdreams.imageserver.utils

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import java.io.File

fun main() {
    val http = HttpClient(Apache)

    runBlocking {
        if (false) {
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

        val r = http.post<HttpResponse>("https://gabriela.loritta.website/api/v1/images/ship") {
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
                }

                put("percentage", 99)
            }.toString()
        }

        println(r.status)
        println(r)

        File("L:\\LorittaAssets\\GabrielaImageGen\\temp\\ship.png")
            .writeBytes(r.readBytes())
    }
}