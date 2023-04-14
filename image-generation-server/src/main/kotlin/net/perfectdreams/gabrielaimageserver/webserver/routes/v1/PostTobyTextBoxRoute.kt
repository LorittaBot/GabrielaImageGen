package net.perfectdreams.gabrielaimageserver.webserver.routes.v1

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import mu.KotlinLogging
import net.perfectdreams.gabrielaimageserver.data.TobyTextBoxRequest
import net.perfectdreams.gabrielaimageserver.generators.undertale.textbox.CharacterPortrait
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.data.SourceImagesContext
import net.perfectdreams.gabrielaimageserver.webserver.data.SourceStringsContext
import net.perfectdreams.gabrielaimageserver.webserver.utils.WebsiteExceptionProcessor

class PostTobyTextBoxRoute(val m: GabrielaImageGen) : VersionedAPIv1Route(
    "/images/toby-text-box"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        try {
            withRequest(logger) {
                val postResult = call.receiveText()

                val json = Json.parseToJsonElement(postResult)
                    .jsonObject

                val generator = when (val type = json["type"]!!.jsonPrimitive.content) {
                    "ORIGINAL" -> m.generators.tobyTextBoxGenerators.undertaleTextBoxGenerator
                    "DARK_WORLD" -> m.generators.tobyTextBoxGenerators.darkWorldTextBoxGenerator
                    else -> error("Unsupported type $type!")
                }

                val portraitString = json["portrait"]?.jsonPrimitive?.content

                val portrait = if (portraitString != null)
                    m.generators.tobyTextBoxGenerators.portraits[portraitString] ?: error("Portrait $portraitString does not exist!")
                else {
                    val imagesContext = SourceImagesContext.fromOrNull(m.connectionManager, postResult)
                    if (imagesContext?.images?.isNotEmpty() == true)
                        CharacterPortrait.fromCustom(imagesContext.retrieveImage(0), TobyTextBoxRequest.ColorPortraitType.valueOf(json["colorPortraitType"]!!.jsonPrimitive.content))
                    else
                        null
                }

                val stringsContext = SourceStringsContext.from(postResult)
                val input = stringsContext.retrieveString(0)

                val result = withContext(m.coroutineDispatcher) {
                    generator.generate(
                        input,
                        portrait
                    )
                }

                call.respondBytes(result, ContentType.Image.GIF)
            }
        } catch (e: Throwable) {
            WebsiteExceptionProcessor.handle(e)
        }
    }
}