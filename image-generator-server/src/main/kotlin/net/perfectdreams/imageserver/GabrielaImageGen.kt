package net.perfectdreams.imageserver

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import mu.KotlinLogging
import net.perfectdreams.imageserver.config.AppConfig
import net.perfectdreams.imageserver.generators.Generators
import net.perfectdreams.imageserver.routes.PostCarlyAaahRoute
import net.perfectdreams.imageserver.routes.PostPetPetRoute
import net.perfectdreams.imageserver.routes.drake.DrakeRoutes
import net.perfectdreams.imageserver.routes.scaled.ScaledRoutes
import net.perfectdreams.imageserver.routes.skewed.SkewedRoutes
import net.perfectdreams.imageserver.utils.Gifsicle
import java.io.File
import java.util.*
import java.util.concurrent.Executors
import javax.imageio.ImageIO

class GabrielaImageGen(val config: AppConfig) {
    companion object {
        val GIFSICLE_PATH = File("/usr/bin/gifsicle")

        private val logger = KotlinLogging.logger {}
    }

    val scheduledThreadPool = Executors.newScheduledThreadPool(16)
    val coroutineDispatcher = scheduledThreadPool.asCoroutineDispatcher()

    val generators = Generators(this)
    val gifsicle = Gifsicle(File(config.gifsiclePath))

    val routes = listOf(
        PostCarlyAaahRoute(this),
        PostPetPetRoute(this),
        *SkewedRoutes(this).all().toTypedArray(),
        *ScaledRoutes(this).all().toTypedArray(),
        *DrakeRoutes(this).all().toTypedArray()
    )

    fun start() {
        val server = embeddedServer(Netty, port = 8001) {
            routing {
                get("/") {
                    call.respondText("Hello World!", ContentType.Text.Plain)
                }

                for (route in routes)
                    route.register(this)
            }
        }
        server.start(wait = true)
    }
}