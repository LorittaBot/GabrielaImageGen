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
import kotlinx.coroutines.*
import mu.KotlinLogging
import net.perfectdreams.gabrielaimageserver.utils.Gifsicle
import net.perfectdreams.gabrielaimageserver.webserver.config.AppConfig
import net.perfectdreams.gabrielaimageserver.webserver.generators.Generators
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.*
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.PostCocieloChavesRoute
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.PostManiaTitleCardRoute
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.PostSAMLogoRoute
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.PostShipRoute
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.PostTerminatorAnimeRoute
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.PostTobyTextBoxRoute
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.cortesflow.CortesFlowRoutes
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.cortesflow.GetCortesFlowRoute
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.drake.DrakeRoutes
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.scaled.ScaledRoutes
import net.perfectdreams.gabrielaimageserver.webserver.routes.v1.skewed.SkewedRoutes
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
        // ===[ API V1 ]===
        PostCarlyAaahRoute(this),
        net.perfectdreams.gabrielaimageserver.webserver.routes.v1.PostPetPetRoute(this),
        PostAttackOnHeartRoute(this),
        PostCocieloChavesRoute(this),
        GetCortesFlowRoute(this),
        PostManiaTitleCardRoute(this),
        PostCepoDeMadeiraRoute(this),
        PostGetOverHereRoute(this),
        PostKnucklesThrowRoute(this),
        PostNichijouYuukoPaperRoute(this),
        PostTrumpRoute(this),
        PostTerminatorAnimeRoute(this),
        PostSAMLogoRoute(this),
        PostToBeContinuedGeneratorRoute(this),
        PostInvertColorsGeneratorRoute(this),
        PostFansExplainingGeneratorRoute(this),
        PostMemeMakerGeneratorRoute(this),
        *SkewedRoutes(this).all().toTypedArray(),
        *ScaledRoutes(this).all().toTypedArray(),
        *DrakeRoutes(this).all().toTypedArray(),
        *CortesFlowRoutes(this).all().toTypedArray(),
        PostTobyTextBoxRoute(this),

        // ===[ API V2 ]===
        net.perfectdreams.gabrielaimageserver.webserver.routes.v2.PostShipRoute(this),
        net.perfectdreams.gabrielaimageserver.webserver.routes.v2.PostPetPetRoute(this),
        net.perfectdreams.gabrielaimageserver.webserver.routes.v2.PostManiaTitleCardRoute(this),
        net.perfectdreams.gabrielaimageserver.webserver.routes.v2.PostTobyTextBoxRoute(this),
        net.perfectdreams.gabrielaimageserver.webserver.routes.v2.PostFansExplainingRoute(this),
        net.perfectdreams.gabrielaimageserver.webserver.routes.v2.PostCocieloChavesRoute(this),
        PostMemeMakerRoute(this),
        net.perfectdreams.gabrielaimageserver.webserver.routes.v2.PostSAMLogoRoute(this),
        net.perfectdreams.gabrielaimageserver.webserver.routes.v2.PostTerminatorAnimeRoute(this),
        net.perfectdreams.gabrielaimageserver.webserver.routes.v2.PostMinecraftSkinLorittaSweatshirtRoute(this),
        PostShipRoute(this),
        PostGigaChadRoute(this),
        PostDrawnMaskAtendenteRoute(this),
        PostDrawnMaskWordRoute(this),
        PostSadRealityRoute(this),
        PostColorInfoRoute(this),

        *net.perfectdreams.gabrielaimageserver.webserver.routes.v2.CortesFlowRoutes(this).all().toTypedArray(),
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