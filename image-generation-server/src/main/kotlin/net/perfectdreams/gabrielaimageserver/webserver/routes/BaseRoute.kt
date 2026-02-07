package net.perfectdreams.gabrielaimageserver.webserver.routes

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.routing.*

abstract class BaseRoute(val path: String) {
    abstract suspend fun onRequest(call: ApplicationCall)

    fun register(routing: Routing) = registerWithPath(routing, path) { onRequest(call) }

    fun registerWithPath(routing: Routing, path: String, callback: suspend RoutingContext.() -> Unit) {
        val method = getMethod()
        when (method) {
            HttpMethod.Get -> routing.get(path, callback)
            HttpMethod.Post -> routing.post(path, callback)
            HttpMethod.Patch -> routing.patch(path, callback)
            HttpMethod.Put -> routing.put(path, callback)
            HttpMethod.Delete -> routing.delete(path, callback)
            else -> routing.get(path, callback)
        }
    }

    open fun getMethod(): HttpMethod {
        val className = this::class.simpleName?.lowercase() ?: "Unknown"
        return when {
            className.startsWith("get") -> HttpMethod.Get
            className.startsWith("post") -> HttpMethod.Post
            className.startsWith("patch") -> HttpMethod.Patch
            className.startsWith("put") -> HttpMethod.Put
            className.startsWith("delete") -> HttpMethod.Delete
            else -> HttpMethod.Get
        }
    }
}
