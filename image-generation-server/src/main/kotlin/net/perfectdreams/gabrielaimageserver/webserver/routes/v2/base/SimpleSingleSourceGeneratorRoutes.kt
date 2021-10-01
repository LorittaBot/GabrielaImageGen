package net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base

import io.ktor.http.*
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.generators.Generators
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleSingleImageRoute.Companion.createImage
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleSingleImageRoute.Companion.createVideo

/**
 * Routes that don't require any special *magic* to make them work. (So, stuff that inherits [SingleSourceImageToByteArrayGenerator])
 */
class SimpleSingleSourceGeneratorRoutes(val m: GabrielaImageGen) {
    fun all() = videos() + simple() + scaled() + skewed()

    fun videos() = listOf(
        createVideo(m, ContentType.Video.MP4, Generators::attackOnHeartGenerator),
        createVideo(m, ContentType.Video.MP4, Generators::carlyAaahGenerator)
    )

    fun simple() = listOf(
        createImage(m, ContentType.Image.PNG, Generators::toBeContinuedGenerator),
        createImage(m, ContentType.Image.PNG, Generators::invertColorsGenerator),
        createImage(m, ContentType.Image.GIF, Generators::cepoDeMadeiraGenerator),
        createImage(m, ContentType.Image.GIF, Generators::knucklesThrowGenerator),
        createImage(m, ContentType.Image.GIF, Generators::nichijouYuukoPaperGenerator),
        createImage(m, ContentType.Image.GIF, Generators::getOverHereGenerator)
    )

    fun scaled() = listOf(
        createImage(m, ContentType.Image.PNG, Generators::pepeDreamGenerator),
        createImage(m, ContentType.Image.PNG, Generators::loriScaredGenerator),
        createImage(m, ContentType.Image.PNG, Generators::studiopolisTVGenerator)
    )

    fun skewed() = listOf(
        createImage(m, ContentType.Image.PNG, Generators::artGenerator),
        createImage(m, ContentType.Image.PNG, Generators::bobBurningPaperGenerator),
        createImage(m, ContentType.Image.PNG, Generators::bolsoFrameGenerator),
        createImage(m, ContentType.Image.PNG, Generators::bolsonaroGenerator),
        createImage(m, ContentType.Image.PNG, Generators::bolsonaro2Generator),
        createImage(m, ContentType.Image.PNG, Generators::briggsCoverGenerator),
        createImage(m, ContentType.Image.PNG, Generators::buckShirtGenerator),
        createImage(m, ContentType.Image.PNG, Generators::canellaDVDGenerator),
        createImage(m, ContentType.Image.PNG, Generators::chicoAtaGenerator),
        createImage(m, ContentType.Image.PNG, Generators::ednaldoBandeiraGenerator),
        createImage(m, ContentType.Image.PNG, Generators::ednaldoTVGenerator),
        createImage(m, ContentType.Image.PNG, Generators::gessyAtaGenerator),
        createImage(m, ContentType.Image.PNG, Generators::loriAtaGenerator),
        createImage(m, ContentType.Image.PNG, Generators::loriSignGenerator),
        createImage(m, ContentType.Image.PNG, Generators::monicaAtaGenerator),
        createImage(m, ContentType.Image.PNG, Generators::passingPaperGenerator),
        createImage(m, ContentType.Image.PNG, Generators::romeroBrittoGenerator),
        createImage(m, ContentType.Image.PNG, Generators::wolverineFrameGenerator),
        createImage(m, ContentType.Image.PNG, Generators::ripTvGenerator)
    )
}