package net.perfectdreams.gabrielaimageserver.webserver.utils

import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import mu.KotlinLogging
import net.perfectdreams.gabrielaimageserver.data.ContentLengthTooLargeExceptionResponse
import net.perfectdreams.gabrielaimageserver.data.ErrorResponse
import net.perfectdreams.gabrielaimageserver.data.ImageNotFoundExceptionResponse
import net.perfectdreams.gabrielaimageserver.data.ImageTooLargeExceptionResponse
import net.perfectdreams.gabrielaimageserver.data.InternalServerErrorExceptionResponse
import net.perfectdreams.gabrielaimageserver.data.InvalidChavesOpeningTextExceptionResponse
import net.perfectdreams.gabrielaimageserver.data.InvalidMinecraftSkinExceptionResponse
import net.perfectdreams.gabrielaimageserver.data.UntrustedURLExceptionResponse
import net.perfectdreams.gabrielaimageserver.exceptions.ContentLengthTooLargeException
import net.perfectdreams.gabrielaimageserver.exceptions.ImageNotFoundException
import net.perfectdreams.gabrielaimageserver.exceptions.ImageTooLargeException
import net.perfectdreams.gabrielaimageserver.exceptions.InvalidChavesOpeningTextException
import net.perfectdreams.gabrielaimageserver.exceptions.InvalidMinecraftSkinException
import net.perfectdreams.gabrielaimageserver.exceptions.StreamExceedsLimitException
import net.perfectdreams.gabrielaimageserver.exceptions.UntrustedURLException

object WebsiteExceptionProcessor {
    private val logger = KotlinLogging.logger {}

    fun handle(e: Throwable) {
        logger.warn(e) { "Something went wrong!" }

        when (e) {
            is ContentLengthTooLargeException -> throw WebsiteAPIException(
                HttpStatusCode.UnprocessableEntity,
                Json.encodeToJsonElement<ErrorResponse>(
                    ContentLengthTooLargeExceptionResponse(e.message)
                )
            )
            is ImageTooLargeException -> throw WebsiteAPIException(
                HttpStatusCode.UnprocessableEntity,
                Json.encodeToJsonElement<ErrorResponse>(
                    ImageTooLargeExceptionResponse(e.message)
                )
            )
            is StreamExceedsLimitException -> throw WebsiteAPIException(
                HttpStatusCode.UnprocessableEntity,
                Json.encodeToJsonElement<ErrorResponse>(
                    ContentLengthTooLargeExceptionResponse(e.message)
                )
            )
            is UntrustedURLException -> throw WebsiteAPIException(
                HttpStatusCode.UnprocessableEntity,
                Json.encodeToJsonElement<ErrorResponse>(
                    UntrustedURLExceptionResponse(e.message, e.url)
                )
            )
            is ImageNotFoundException -> throw WebsiteAPIException(
                HttpStatusCode.UnprocessableEntity,
                Json.encodeToJsonElement<ErrorResponse>(
                    ImageNotFoundExceptionResponse(e.message)
                )
            )
            is InvalidMinecraftSkinException -> throw WebsiteAPIException(
                HttpStatusCode.UnprocessableEntity,
                Json.encodeToJsonElement<ErrorResponse>(
                    InvalidMinecraftSkinExceptionResponse(e.message)
                )
            )
            is InvalidChavesOpeningTextException -> throw WebsiteAPIException(
                HttpStatusCode.UnprocessableEntity,
                Json.encodeToJsonElement<ErrorResponse>(
                    InvalidChavesOpeningTextExceptionResponse(e.message)
                )
            )
            else -> throw WebsiteAPIException(
                HttpStatusCode.InternalServerError,
                Json.encodeToJsonElement<ErrorResponse>(
                    InternalServerErrorExceptionResponse(e.message)
                )
            )
        }
    }
}