package net.perfectdreams.imageserver.routes.skewed

import io.ktor.http.*
import net.perfectdreams.imagegen.generators.BasicSkewedImageGenerator
import net.perfectdreams.imagegen.generators.skewed.ArtGenerator
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.generators.Generators
import net.perfectdreams.imageserver.routes.RouteUtils
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

    fun convertToSnakeCase(input: String): String {
        val x = input.removeSuffix("Generator")

        val newString = StringBuilder()

        for (index in x.indices) {
            val charAt = x[index]
            val nextChar = x.getOrNull(index + 1)

            if (charAt.isLowerCase() && nextChar?.isUpperCase() == true) {
                newString.append(charAt.toLowerCase())
                newString.append("_")
            } else {
                newString.append(charAt.toLowerCase())
            }
        }

        return newString.toString()
    }
}