package net.perfectdreams.gabrielaimageserver.webserver.routes.v1.cortesflow

import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put
import io.github.oshai.kotlinlogging.KotlinLogging
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.VersionedAPIv1Route

open class GetCortesFlowRoute(val m: GabrielaImageGen) : VersionedAPIv1Route(
    "/images/cortes-flow"
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun onRequest(call: ApplicationCall) {
        // Show all available cortes
        val cortesFlowRoutes = m.routes.filterIsInstance<PostCortesFlowRoute>()

        val jsonArray = buildJsonArray {
            for (route in cortesFlowRoutes) {
                addJsonObject {
                    put("path", route.path)
                    put("participant", route.generator.participant.name)
                    put("participantDisplayName", route.generator.participant.displayName)
                    put("source", route.generator.source)
                }
            }
        }

        call.respondText(jsonArray.toString())
    }
}