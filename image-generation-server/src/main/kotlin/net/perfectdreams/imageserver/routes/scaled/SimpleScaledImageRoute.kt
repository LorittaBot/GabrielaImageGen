package net.perfectdreams.imageserver.routes.scaled

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.imagegen.generators.BasicScaledImageGenerator
import net.perfectdreams.imagegen.graphics.Image
import net.perfectdreams.imagegen.graphics.JVMImage
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.routes.PostSingleSourceImageGeneratorRoute
import net.perfectdreams.imageserver.routes.VersionedAPIRoute
import net.perfectdreams.imageserver.utils.extensions.getImageDataContext
import net.perfectdreams.imageserver.utils.WebsiteExceptionProcessor
import net.perfectdreams.imageserver.utils.extensions.retrieveImageFromImageData
import java.util.*

abstract class SimpleScaledImageRoute(m: GabrielaImageGen, generator: BasicScaledImageGenerator, path: String) : PostSingleSourceImageGeneratorRoute(
    m,
    generator,
    ContentType.Image.PNG,
    "/images/$path"
)