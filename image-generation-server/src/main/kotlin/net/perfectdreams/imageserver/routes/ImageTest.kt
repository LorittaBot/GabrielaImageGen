package net.perfectdreams.imageserver.routes

import io.ktor.application.*
import io.ktor.request.*
import net.perfectdreams.imageserver.data.SourceImagesContext
import net.perfectdreams.imageserver.data.SourceStringData
import net.perfectdreams.imageserver.data.SourceStringsContext

suspend fun ApplicationCall.getImageDataContext(): SourceImagesContext {
    val postResult = receiveText()

    println(postResult)
    return SourceImagesContext.from(postResult)
}

suspend fun ApplicationCall.getStringDataContext(): SourceStringsContext {
    val postResult = receiveText()

    println(postResult)
    return SourceStringsContext.from(postResult)
}