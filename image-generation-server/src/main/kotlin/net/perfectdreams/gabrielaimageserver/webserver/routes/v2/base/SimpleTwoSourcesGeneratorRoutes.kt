package net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base

import io.ktor.http.*
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.generators.Generators
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleTwoImagesRoute.Companion.createImage

/**
 * Routes that don't require any special *magic* to make them work. (So, stuff that inherits [TwoSourceImagesToByteArrayGenerator])
 */
class SimpleTwoSourcesGeneratorRoutes(val m: GabrielaImageGen) {
    fun all() = simple() + drake()

    fun simple() = listOf(
        createImage(m, ContentType.Image.GIF, Generators::trumpGenerator),
    )

    fun drake() = listOf(
        createImage(m, ContentType.Image.PNG, Generators::drakeGenerator),
        createImage(m, ContentType.Image.PNG, Generators::bolsoDrakeGenerator),
        createImage(m, ContentType.Image.PNG, Generators::loriDrakeGenerator),
    )
}