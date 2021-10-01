package net.perfectdreams.gabrielaimageserver.webserver.utils.extensions

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.*
import kotlinx.serialization.json.JsonElement

val handledStatusBefore = AttributeKey<Boolean>("handledStatusBefore")

var ApplicationCall.alreadyHandledStatus: Boolean
    get() = this.attributes.getOrNull(handledStatusBefore) ?: false
    set(value) = this.attributes.put(handledStatusBefore, value)

suspend fun ApplicationCall.respondJson(json: JsonElement, status: HttpStatusCode? = null) = this.respondText(ContentType.Application.Json, status) {
    json.toString()
}