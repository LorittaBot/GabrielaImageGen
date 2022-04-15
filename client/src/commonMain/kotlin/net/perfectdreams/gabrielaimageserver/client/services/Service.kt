package net.perfectdreams.gabrielaimageserver.client.services

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import net.perfectdreams.gabrielaimageserver.client.GabrielaImageServerClient
import net.perfectdreams.gabrielaimageserver.data.ContentLengthTooLargeExceptionResponse
import net.perfectdreams.gabrielaimageserver.data.ErrorResponse
import net.perfectdreams.gabrielaimageserver.data.ImageNotFoundExceptionResponse
import net.perfectdreams.gabrielaimageserver.data.ImageTooLargeExceptionResponse
import net.perfectdreams.gabrielaimageserver.data.InternalServerErrorExceptionResponse
import net.perfectdreams.gabrielaimageserver.data.InvalidMinecraftSkinExceptionResponse
import net.perfectdreams.gabrielaimageserver.data.StreamExceedsLimitExceptionResponse
import net.perfectdreams.gabrielaimageserver.data.UntrustedURLExceptionResponse
import net.perfectdreams.gabrielaimageserver.exceptions.ContentLengthTooLargeException
import net.perfectdreams.gabrielaimageserver.exceptions.ImageNotFoundException
import net.perfectdreams.gabrielaimageserver.exceptions.ImageTooLargeException
import net.perfectdreams.gabrielaimageserver.exceptions.InternalServerErrorException
import net.perfectdreams.gabrielaimageserver.exceptions.InvalidMinecraftSkinException
import net.perfectdreams.gabrielaimageserver.exceptions.StreamExceedsLimitException
import net.perfectdreams.gabrielaimageserver.exceptions.UntrustedURLException

open class Service(private val client: GabrielaImageServerClient) {
    private val apiVersion = "v2"

    suspend inline fun <reified T> execute(endpoint: String, body: T) = execute(endpoint, Json.encodeToJsonElement<T>(body).jsonObject)

    suspend fun execute(endpoint: String, body: JsonObject): ByteArray {
        val response = client.http.post("${client.baseUrl}/api/$apiVersion$endpoint") {
            this.setBody(body.toString())
        }

        // If the status code is not between 400..499, then it means that it was (probably) a invalid input or something
        if (response.status.value !in 200..299) {
            // If it wasn't successful, let's try parsing the response!
            val errorResponse = Json.decodeFromString<ErrorResponse>(response.bodyAsText())

            when (errorResponse) {
                is ContentLengthTooLargeExceptionResponse -> throw ContentLengthTooLargeException()
                is ImageNotFoundExceptionResponse -> throw ImageNotFoundException()
                is InvalidMinecraftSkinExceptionResponse -> throw InvalidMinecraftSkinException()
                is ImageTooLargeExceptionResponse -> throw ImageTooLargeException()
                is InternalServerErrorExceptionResponse -> throw InternalServerErrorException()
                is StreamExceedsLimitExceptionResponse -> throw StreamExceedsLimitException()
                is UntrustedURLExceptionResponse -> throw UntrustedURLException(errorResponse.url)
            }
        }

        return response.body()
    }
}