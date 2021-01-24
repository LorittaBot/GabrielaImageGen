package net.perfectdreams.imageserver.routes.drake

import io.ktor.http.*
import net.perfectdreams.imagegen.generators.BasicDrakeImageGenerator
import net.perfectdreams.imagegen.generators.BasicScaledImageGenerator
import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.generators.Generators
import net.perfectdreams.imageserver.routes.RouteUtils
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