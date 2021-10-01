package net.perfectdreams.gabrielaimageserver.webserver.routes.v1.scaled

import io.ktor.http.*
import net.perfectdreams.gabrielaimageserver.generators.BasicScaledImageGenerator
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.generators.Generators
import net.perfectdreams.gabrielaimageserver.webserver.routes.RouteUtils
import kotlin.reflect.KProperty1

class ScaledRoutes(val m: GabrielaImageGen) {
    fun all() = listOf(
        create(Generators::pepeDreamGenerator),
        create(Generators::loriScaredGenerator),
        create(Generators::studiopolisTVGenerator)
    )

    fun create(property: KProperty1<Generators, BasicScaledImageGenerator>): SimpleScaledImageRoute {
        val value = property.get(m.generators)

        return object: SimpleScaledImageRoute(
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