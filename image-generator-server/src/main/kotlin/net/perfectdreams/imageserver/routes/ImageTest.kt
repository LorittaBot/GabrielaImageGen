package net.perfectdreams.imageserver.routes

import io.ktor.application.*
import io.ktor.request.*
import net.perfectdreams.imageserver.data.SourceImagesContext

suspend fun ApplicationCall.getImageDataContext(): SourceImagesContext {
    val postResult = receiveText()

    println(postResult)
    return SourceImagesContext.from(postResult)
}