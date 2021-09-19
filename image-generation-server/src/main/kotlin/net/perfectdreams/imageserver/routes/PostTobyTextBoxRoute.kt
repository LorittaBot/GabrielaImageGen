package net.perfectdreams.imageserver.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import mu.KotlinLogging
import net.perfectdreams.imagegen.generators.undertale.textbox.CharacterPortrait
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.data.SourceImagesContext
import net.perfectdreams.imageserver.data.SourceStringsContext
import net.perfectdreams.imageserver.utils.WebsiteExceptionProcessor

class PostTobyTextBoxRoute(val m: GabrielaImageGen) : VersionedAPIRoute(
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
                    val imagesContext = SourceImagesContext.from(m.connectionManager, postResult)
                    if (imagesContext.images.isNotEmpty())
                        CharacterPortrait.fromCustom(imagesContext.retrieveImage(0))
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