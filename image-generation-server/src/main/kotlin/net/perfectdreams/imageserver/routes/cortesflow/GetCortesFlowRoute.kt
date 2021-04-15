package net.perfectdreams.imageserver.routes.cortesflow

import io.ktor.application.*
import io.ktor.response.*
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put
import mu.KotlinLogging
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.routes.VersionedAPIRoute

open class GetCortesFlowRoute(val m: GabrielaImageGen) : VersionedAPIRoute(
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