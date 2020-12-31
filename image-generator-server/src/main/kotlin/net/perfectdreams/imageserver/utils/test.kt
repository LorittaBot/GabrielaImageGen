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
        val r = http.post<HttpResponse>("http://127.0.0.1:8001/api/v1/images/monica-ata") {
            body = buildJsonObject {
                putJsonArray("images") {
                    addJsonObject {
                        put("type", "url")
                        put("content", "https://upload.wikimedia.org/wikipedia/commons/6/6f/CMB_Timeline300_no_WMAP.jpg")
                    }
                }
            }.toString()
        }

        println(r)

        File("C:\\Users\\Leonardo\\Documents\\LorittaAssets\\GabrielaImageGen\\temp\\douglas_pointing.jpg")
            .writeBytes(r.readBytes())
    }
}