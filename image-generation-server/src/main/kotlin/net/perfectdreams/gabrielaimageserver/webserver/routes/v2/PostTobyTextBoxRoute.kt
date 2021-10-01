package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.TobyTextBoxRequest
import net.perfectdreams.gabrielaimageserver.generators.undertale.textbox.CharacterPortrait
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleAPIv2Route
import net.perfectdreams.gabrielaimageserver.webserver.utils.retrieveImage

class PostTobyTextBoxRoute(m: GabrielaImageGen) : SimpleAPIv2Route<TobyTextBoxRequest>(
    "/images/toby-text-box",
    m,
    ContentType.Image.GIF
) {
    override val deserializationBlock: (String) -> TobyTextBoxRequest = { Json.decodeFromString(it) }

    override suspend fun generate(data: TobyTextBoxRequest): ByteArray {
        val (text, type, portraitString, image, colorPortraitType) = data

        val generator = when (data.type) {
            TobyTextBoxRequest.TextBoxType.ORIGINAL -> m.generators.tobyTextBoxGenerators.undertaleTextBoxGenerator
            TobyTextBoxRequest.TextBoxType.DARK_WORLD -> m.generators.tobyTextBoxGenerators.darkWorldTextBoxGenerator
        }

        val portrait = if (portraitString != null)
            m.generators.tobyTextBoxGenerators.portraits[portraitString] ?: error("Portrait $portraitString does not exist!")
        else {
            if (image != null && colorPortraitType != null)
                CharacterPortrait.fromCustom(image.retrieveImage(m.connectionManager), colorPortraitType)
            else
                null
        }

        return measureGeneratorLatency(generator) {
            generator.generate(
                text,
                portrait
            )
        }
    }
}