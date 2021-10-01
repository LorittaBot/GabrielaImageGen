package net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base

import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.SingleImageRequest
import net.perfectdreams.gabrielaimageserver.generators.SingleSourceImageToByteArrayGenerator
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.generators.Generators
import net.perfectdreams.gabrielaimageserver.webserver.routes.RouteUtils
import net.perfectdreams.gabrielaimageserver.webserver.utils.retrieveImage
import kotlin.reflect.KProperty1

open class SimpleSingleImageRoute(
    m: GabrielaImageGen,
    path: String,
    type: ContentType,
    val generator: SingleSourceImageToByteArrayGenerator,
) : SimpleAPIv2Route<SingleImageRequest>(
    path,
    m,
    type
) {
    companion object {
        fun createImage(m: GabrielaImageGen, type: ContentType, property: KProperty1<Generators, SingleSourceImageToByteArrayGenerator>): SimpleSingleImageRoute {
            val value = property.get(m.generators)

            return object: SimpleSingleImageRoute(
                m,
                "/images/" + RouteUtils.convertToKebabCase(property.name)
                    .replace("_", "-"),
                type,
                value
            ) {}
        }

        fun createVideo(m: GabrielaImageGen, type: ContentType, property: KProperty1<Generators, SingleSourceImageToByteArrayGenerator>): SimpleSingleImageRoute {
            val value = property.get(m.generators)

            return object: SimpleSingleImageRoute(
                m,
                "/videos/" + RouteUtils.convertToKebabCase(property.name)
                    .replace("_", "-"),
                type,
                value
            ) {}
        }
    }

    override val deserializationBlock: (String) -> SingleImageRequest = { Json.decodeFromString(it) }

    override suspend fun generate(data: SingleImageRequest): ByteArray {
        val (imageData) = data
        val image = imageData.retrieveImage(m.connectionManager)

        return measureGeneratorLatency(generator) {
            generate(image)
        }
    }

    // We need to override the method because we will create anonymous classes, so the method is set as "Get"
    override fun getMethod() = HttpMethod.Post
}