package net.perfectdreams.imageserver.utils

import io.ktor.http.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import mu.KotlinLogging

object WebsiteExceptionProcessor {
    private val logger = KotlinLogging.logger {}

    fun handle(e: Throwable) {
        logger.warn(e) { "Something went wrong!" }

        when (e) {
            is ImageUtils.ContentLengthTooLargeException -> throw WebsiteAPIException(
                    HttpStatusCode.UnprocessableEntity,
                    buildJsonObject {
                        putJsonObject("error") {
                            put("reason", "ContentLengthTooLargeException")
                        }
                    }
            )
            is ImageUtils.ImageTooLargeException -> throw WebsiteAPIException(
                    HttpStatusCode.UnprocessableEntity,
                    buildJsonObject {
                        putJsonObject("error") {
                            put("reason", "ImageTooLargeException")
                        }
                    }
            )
            is StreamExceedsLimitException -> throw WebsiteAPIException(
                    HttpStatusCode.UnprocessableEntity,
                    buildJsonObject {
                        putJsonObject("error") {
                            put("reason", "StreamExceedsLimitException")
                        }
                    }
            )
            is UntrustedURLException -> throw WebsiteAPIException(
                HttpStatusCode.UnprocessableEntity,
                buildJsonObject {
                    putJsonObject("error") {
                        put("reason", "UntrustedURLException")
                    }
                }
            )
            else -> throw WebsiteAPIException(
                    HttpStatusCode.InternalServerError,
                    buildJsonObject {
                        putJsonObject("error") {
                            put("reason", e.message)
                        }
                    }
            )
        }
    }
}