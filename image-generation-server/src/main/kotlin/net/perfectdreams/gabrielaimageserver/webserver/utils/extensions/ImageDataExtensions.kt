package net.perfectdreams.gabrielaimageserver.webserver.utils.extensions

import io.ktor.server.application.*
import io.ktor.server.request.*
import net.perfectdreams.gabrielaimageserver.webserver.data.SourceImagesContext
import net.perfectdreams.gabrielaimageserver.webserver.data.SourceStringsContext
import net.perfectdreams.gabrielaimageserver.webserver.utils.ConnectionManager

suspend fun ApplicationCall.getImageDataContext(connectionManager: ConnectionManager): SourceImagesContext {
    val postResult = receiveText()

    println(postResult)
    return SourceImagesContext.from(connectionManager, postResult)
}

/**
 * This is a shorthand for [getImageDataContext] and [SourceImagesContext.retrieveImage].
 *
 * Useful if you only need to pull one image data from the context!
 *
 * @param index index of the image
 * @return the image from the context
 */
suspend fun ApplicationCall.retrieveImageFromImageData(connectionManager: ConnectionManager, index: Int) = getImageDataContext(connectionManager).retrieveImage(index)

suspend fun ApplicationCall.getStringDataContext(): SourceStringsContext {
    val postResult = receiveText()

    println(postResult)
    return SourceStringsContext.from(postResult)
}

/**
 * This is a shorthand for [getStringDataContext] and [SourceStringsContext.retrieveString].
 *
 * Useful if you only need to pull one string from the context!
 *
 * @param index index of the string
 * @return the string from the context
 */
suspend fun ApplicationCall.retrieveStringFromStringData(index: Int) = getStringDataContext().retrieveString(index)
