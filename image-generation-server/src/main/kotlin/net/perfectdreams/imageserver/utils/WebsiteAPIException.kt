package net.perfectdreams.imageserver.utils

import io.ktor.http.*
import kotlinx.serialization.json.JsonElement

class WebsiteAPIException(val status: HttpStatusCode, val payload: JsonElement) : RuntimeException()