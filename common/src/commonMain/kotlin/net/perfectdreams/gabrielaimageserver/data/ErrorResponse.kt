package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
sealed class ErrorResponse {
    abstract val reason: String?
}

@Serializable
data class ContentLengthTooLargeExceptionResponse(override val reason: String?) : ErrorResponse()

@Serializable
data class ImageTooLargeExceptionResponse(override val reason: String?) : ErrorResponse()

@Serializable
data class StreamExceedsLimitExceptionResponse(override val reason: String?) : ErrorResponse()

@Serializable
data class UntrustedURLExceptionResponse(override val reason: String?, val url: String) : ErrorResponse()

@Serializable
data class ImageNotFoundExceptionResponse(override val reason: String?) : ErrorResponse()

@Serializable
data class InternalServerErrorExceptionResponse(override val reason: String?) : ErrorResponse()

@Serializable
data class InvalidMinecraftSkinExceptionResponse(override val reason: String?) : ErrorResponse()