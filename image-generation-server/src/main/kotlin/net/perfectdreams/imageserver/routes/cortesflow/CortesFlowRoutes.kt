package net.perfectdreams.imageserver.routes.cortesflow

import io.ktor.http.*
import net.perfectdreams.imagegen.generators.cortesflow.CortesFlowGenerator
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imagegen.generators.CortesFlowGenerators
import net.perfectdreams.imageserver.routes.RouteUtils
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf

class CortesFlowRoutes(val m: GabrielaImageGen) {
    // Automatically creates the routes using Reflection
    // The routes are created from the generators in the CortesFlowGenerator class
    fun all() = m.generators.cortesFlowGenerators::class.members.filter {
        // We want to get only the fields that returns a type of CortesFlowGenerator
        it.returnType.classifier?.createType()?.isSubtypeOf(CortesFlowGenerator::class.createType()) == true
    }.map { create(it as KProperty1<CortesFlowGenerators, CortesFlowGenerator>) }

    fun create(property: KProperty1<CortesFlowGenerators, CortesFlowGenerator>): PostCortesFlowRoute {
        val value = property.get(m.generators.cortesFlowGenerators)

        return object: PostCortesFlowRoute(
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