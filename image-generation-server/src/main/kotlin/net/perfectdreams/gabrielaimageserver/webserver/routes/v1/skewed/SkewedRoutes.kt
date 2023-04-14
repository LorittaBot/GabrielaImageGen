package net.perfectdreams.gabrielaimageserver.webserver.routes.v1.skewed

import io.ktor.http.*
import net.perfectdreams.gabrielaimageserver.generators.BasicSkewedImageGenerator
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.generators.Generators
import net.perfectdreams.gabrielaimageserver.webserver.routes.RouteUtils
import kotlin.reflect.KProperty1

class SkewedRoutes(val m: GabrielaImageGen) {
    fun all() = listOf(
        create(Generators::artGenerator),
        create(Generators::bobBurningPaperGenerator),
        create(Generators::bolsoFrameGenerator),
        create(Generators::bolsonaroGenerator),
        create(Generators::bolsonaro2Generator),
        create(Generators::briggsCoverGenerator),
        create(Generators::buckShirtGenerator),
        create(Generators::canellaDVDGenerator),
        create(Generators::chicoAtaGenerator),
        create(Generators::ednaldoBandeiraGenerator),
        create(Generators::ednaldoTVGenerator),
        create(Generators::gessyAtaGenerator),
        create(Generators::loriAtaGenerator),
        create(Generators::loriSignGenerator),
        create(Generators::monicaAtaGenerator),
        create(Generators::passingPaperGenerator),
        create(Generators::romeroBrittoGenerator),
        create(Generators::wolverineFrameGenerator),
        create(Generators::ripTvGenerator)
    )

    fun create(property: KProperty1<Generators, BasicSkewedImageGenerator>): SimpleSkewedImageRoute {
        val value = property.get(m.generators)

        return object: SimpleSkewedImageRoute(
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