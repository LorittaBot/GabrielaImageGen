package net.perfectdreams.gabrielaimageserver.webserver.routes.v1.drake

import io.ktor.http.*
import net.perfectdreams.gabrielaimageserver.generators.BasicDrakeImageGenerator
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.generators.Generators
import net.perfectdreams.gabrielaimageserver.webserver.routes.RouteUtils
import kotlin.reflect.KProperty1

class DrakeRoutes(val m: GabrielaImageGen) {
    fun all() = listOf(
        create(Generators::drakeGenerator),
        create(Generators::bolsoDrakeGenerator),
        create(Generators::loriDrakeGenerator)
    )

    fun create(property: KProperty1<Generators, BasicDrakeImageGenerator>): SimpleDrakeImageRoute {
        val value = property.get(m.generators)

        return object: SimpleDrakeImageRoute(
            m,
            value,
            RouteUtils.convertToKebabCase(property.name)
                .replace("_", "-")
        ) {
            // We need to override the method because this is a anonymous class, so the method is set as "Get"
            override fun getMethod() = HttpMethod.Post
        }
    }
}