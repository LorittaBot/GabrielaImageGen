package net.perfectdreams.gabrielaimageserver.webserver.routes.v2

import net.perfectdreams.gabrielaimageserver.generators.cortesflow.CortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import net.perfectdreams.gabrielaimageserver.webserver.generators.CortesFlowGenerators
import net.perfectdreams.gabrielaimageserver.webserver.routes.RouteUtils
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleCortesFlowRoute
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

    fun create(property: KProperty1<CortesFlowGenerators, CortesFlowGenerator>): SimpleCortesFlowRoute {
        val value = property.get(m.generators.cortesFlowGenerators)

        return SimpleCortesFlowRoute(
            RouteUtils.convertToKebabCase(property.name)
                .replace("_", "-"),
            m,
            value,
        )
    }
}