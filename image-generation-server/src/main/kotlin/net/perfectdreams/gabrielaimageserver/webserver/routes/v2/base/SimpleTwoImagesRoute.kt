package net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base

import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.TwoImagesRequest
import net.perfectdreams.gabrielaimageserver.generators.TwoSourceImagesToByteArrayGenerator
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.generators.Generators
import net.perfectdreams.gabrielaimageserver.webserver.routes.RouteUtils
import net.perfectdreams.gabrielaimageserver.webserver.utils.retrieveImage
import kotlin.reflect.KProperty1

open class SimpleTwoImagesRoute(
    m: GabrielaImageGen,
    path: String,
    type: ContentType,
    val generator: TwoSourceImagesToByteArrayGenerator,
) : SimpleAPIv2Route<TwoImagesRequest>(
    path,
    m,
    type
) {
    companion object {
        fun createImage(m: GabrielaImageGen, type: ContentType, property: KProperty1<Generators, TwoSourceImagesToByteArrayGenerator>): SimpleTwoImagesRoute {
            val value = property.get(m.generators)

            return object: SimpleTwoImagesRoute(
                m,
                "/images/" + RouteUtils.convertToKebabCase(property.name)
                    .replace("_", "-"),
                type,
                value
            ) {}
        }

        fun createVideo(m: GabrielaImageGen, type: ContentType, property: KProperty1<Generators, TwoSourceImagesToByteArrayGenerator>): SimpleTwoImagesRoute {
            val value = property.get(m.generators)

            return object: SimpleTwoImagesRoute(
                m,
                "/videos/" + RouteUtils.convertToKebabCase(property.name)
                    .replace("_", "-"),
                type,
                value
            ) {}
        }
    }

    override val deserializationBlock: (String) -> TwoImagesRequest = { Json.decodeFromString(it) }

    override suspend fun generate(data: TwoImagesRequest): ByteArray {
        val (imageData) = data
        val image1 = imageData.retrieveImage(m.connectionManager)
        val image2 = imageData.retrieveImage(m.connectionManager)

        return measureGeneratorLatency(generator) {
            generate(image1, image2)
        }
    }

    // We need to override the method because we will create anonymous classes, so the method is set as "Get"
    override fun getMethod() = HttpMethod.Post
}