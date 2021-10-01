package net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base

import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.gabrielaimageserver.data.CortesFlowRequest
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.CortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import java.awt.image.BufferedImage

class SimpleCortesFlowRoute(
    path: String,
    m: GabrielaImageGen,
    val generator: CortesFlowGenerator
) : SimpleBufferedImageAPIv2Route<CortesFlowRequest>(
    "/cortes-flow/$path",
    m
) {
    override val deserializationBlock: (String) -> CortesFlowRequest = { Json.decodeFromString(it) }

    override suspend fun generateImage(data: CortesFlowRequest): BufferedImage {
        val (text) = data

        return measureGeneratorLatency(generator) {
            generate(text)
        }
    }

    override fun getMethod() = HttpMethod.Post
}