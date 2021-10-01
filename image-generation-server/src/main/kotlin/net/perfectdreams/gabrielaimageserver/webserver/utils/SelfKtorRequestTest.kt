package net.perfectdreams.gabrielaimageserver.webserver.utils

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.*
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
    val http = HttpClient(Apache) {
        install(HttpTimeout) {
            this.requestTimeoutMillis = 999999
            this.requestTimeoutMillis = 999999
            this.socketTimeoutMillis = 999999
        }
    }

    runBlocking {
        if (true) {
            return@runBlocking
        }

        if (false) {
            val r = http.post<HttpResponse>("http://127.0.0.1:8001/api/v1/images/toby-text-box") {
                body = buildJsonObject {
                    put("type", "ORIGINAL")
                    put("portrait", "ralsei/neutral")

                    putJsonArray("strings") {
                        addJsonObject {
                            put("string", "ayaya")
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