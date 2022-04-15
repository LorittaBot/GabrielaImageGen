package net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.withContext
import net.perfectdreams.gabrielaimageserver.generators.Generator
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.utils.Prometheus

abstract class SimpleAPIv2Route<T>(
    path: String,
    val m: GabrielaImageGen,
    val type: ContentType
) : VersionedAPIv2Route(
    path
) {
    // Yes, you need to call it manually because "Json.decodeFromString" doesn't work because it needs to be reified, sad...
    abstract val deserializationBlock: (String) -> (T)

    abstract suspend fun generate(data: T): ByteArray

    override suspend fun onRequest(call: ApplicationCall) {
        withRequest {
            val postResult = call.receiveText()

            val data = deserializationBlock.invoke(postResult)
            val result = withContext(m.coroutineDispatcher) {
                generate(data)
            }

            call.respondBytes(result, type)
        }
    }

    suspend fun <T : Generator, R> measureGeneratorLatency(generator: T, block: suspend T.() -> (R)): R {
        val generatorName = generator::class.simpleName!!

        logger.info { "Starting to execute generator $generatorName" }

        val timer = Prometheus.GENERATOR_LATENCY_COUNT
            .labels(generator::class.simpleName!!)
            .startTimer()

        val result = block.invoke(generator)

        val time = timer.observeDuration()

        logger.info { "Finished executing generator $generatorName - Took ${time}s" }
        return result
    }
}