package net.perfectdreams.gabrielaimageserver.webserver

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import net.perfectdreams.gabrielaimageserver.utils.Gifsicle
import net.perfectdreams.gabrielaimageserver.webserver.config.AppConfig
import net.perfectdreams.gabrielaimageserver.webserver.generators.Generators
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.*
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleSingleSourceGeneratorRoutes
import net.perfectdreams.gabrielaimageserver.webserver.routes.v2.base.SimpleTwoSourcesGeneratorRoutes
import net.perfectdreams.gabrielaimageserver.webserver.utils.ConnectionManager
import net.perfectdreams.gabrielaimageserver.webserver.utils.WebsiteAPIException
import net.perfectdreams.gabrielaimageserver.webserver.utils.extensions.alreadyHandledStatus
import net.perfectdreams.gabrielaimageserver.webserver.utils.extensions.respondJson
import java.io.File
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class GabrielaImageGen(val config: AppConfig) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    val scheduledThreadPool = Executors.newScheduledThreadPool(16)
    val coroutineDispatcher = scheduledThreadPool.asCoroutineDispatcher()

    val gifsicle = Gifsicle(File(config.gifsiclePath))
    val generators = Generators(this)

    val connectionManager = ConnectionManager(
        // Move this to a config!
        listOf(
            "discord.gg",
            "discord.com",
            "discordapp.com",
            "discordapp.net",
            "imgur.com",
            "perfectdreams.net",
            "sparklypower.net",
            "loritta.website",
            "lori.fun",
            "loritta.land",
            "twitch.tv",
            "aminoapps.com",
            "youtube.com",
            "youtu.be",
            "ytimg.com",
            "soundcloud.com",
            "maxcdn.com",
            "fbcdn.net",
            "twimg.com",
            "narvii.com",
            "mrpowergamerbr.com",
            "reddit.com",
            "redditmedia.com",
            "redd.it",
            "google.com",
            "googlenews.com",
            "github.com",
            "tenor.com",
            "giphy.com",
            "gfycat.com",
            "twitter.com",
            "facebook.com",
            "minecraft.net",
            "roblox.com",
            "crafatar.com",
            "rbxcdn.com",
            "githubusercontent.com",
            "prntscr.com"
        )
    )

    val routes = listOf(
        // ===[ API V2 ]===
        PostShipRoute(this),
        PostPetPetRoute(this),
        PostManiaTitleCardRoute(this),
        PostTobyTextBoxRoute(this),
        PostFansExplainingRoute(this),
        PostCocieloChavesRoute(this),
        PostMemeMakerRoute(this),
        PostSAMLogoRoute(this),
        PostTerminatorAnimeRoute(this),
        PostMinecraftSkinLorittaSweatshirtRoute(this),
        PostShipRoute(this),
        PostGigaChadRoute(this),
        PostDrawnMaskAtendenteRoute(this),
        PostDrawnMaskWordRoute(this),
        PostSadRealityRoute(this),
        PostGigaChadRoute(this),
        PostColorInfoRoute(this),
        PostChavesOpeningRoute(this),
        *CortesFlowRoutes(this).all().toTypedArray(),
        *SimpleSingleSourceGeneratorRoutes(this).all().toTypedArray(),
        *SimpleTwoSourcesGeneratorRoutes(this).all().toTypedArray()
    )

    fun start() {
        val server = embeddedServer(Netty, port = 8001) {
            install(StatusPages) {
                exception<WebsiteAPIException> { call, cause ->
                    call.alreadyHandledStatus = true
                    call.respondJson(cause.payload, cause.status)
                }
            }

            install(Compression) {
                // Enable Compression for specific types, because Ktor doesn't do that by default
                // https://ktor.io/docs/compression.html#configure_content_type
                matchContentType(
                    ContentType.Text.Any,

                    // TODO: Check if "any" means that it would compress a video tagged as MP4
                    // ===[ VIDEOS ]===
                    ContentType.Video.MP4,
                    ContentType.Video.OGG,
                    ContentType.Video.QuickTime,
                    ContentType.Video.MPEG,
                    ContentType.Video.Any,

                    // ===[ IMAGES ]===
                    ContentType.Image.PNG,
                    ContentType.Image.JPEG,
                    ContentType.Image.Any,
                )
            }

            routing {
                trace {
                    println(it.buildText())
                }

                get("/") {
                    call.respondText("Hello World!", ContentType.Text.Plain)
                }

                static("/assets/img/templates/") {
                    resources("/image_templates")
                }

                for (route in routes)
                    route.register(this)
            }
        }
        server.start(wait = true)

        Runtime.getRuntime().addShutdownHook(
            thread(false) {
                activeJobs.forEach {
                    try {
                        it.cancel()
                    } catch (e: Exception) {}
                }

                server.stop(15_000L, 15_000L)
            }
        )
    }

    private val activeJobs = ConcurrentLinkedQueue<Job>()

    suspend fun launch(block: suspend CoroutineScope.() -> Unit) {
        coroutineScope {
            val job = launch(
                coroutineDispatcher,
                block = block
            )

            // Yes, the order matters, since sometimes the invokeOnCompletion would be invoked before the job was
            // added to the list, causing leaks.
            // invokeOnCompletion is also invoked even if the job was already completed at that point, so no worries!
            activeJobs.add(job)
            job.invokeOnCompletion {
                activeJobs.remove(job)
            }
        }
    }
}