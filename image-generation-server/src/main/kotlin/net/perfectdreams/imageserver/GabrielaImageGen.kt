package net.perfectdreams.imageserver

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.asCoroutineDispatcher
import mu.KotlinLogging
import net.perfectdreams.imageserver.config.AppConfig
import net.perfectdreams.imageserver.generators.Generators
import net.perfectdreams.imageserver.routes.PostAttackOnHeartRoute
import net.perfectdreams.imageserver.routes.PostCarlyAaahRoute
import net.perfectdreams.imageserver.routes.PostCepoDeMadeiraRoute
import net.perfectdreams.imageserver.routes.PostCocieloChavesRoute
import net.perfectdreams.imageserver.routes.PostFansExplainingGeneratorRoute
import net.perfectdreams.imageserver.routes.PostGetOverHereRoute
import net.perfectdreams.imageserver.routes.PostInvertColorsGeneratorRoute
import net.perfectdreams.imageserver.routes.PostKnucklesThrowRoute
import net.perfectdreams.imageserver.routes.PostManiaTitleCardRoute
import net.perfectdreams.imageserver.routes.PostNichijouYuukoPaperRoute
import net.perfectdreams.imageserver.routes.PostPetPetRoute
import net.perfectdreams.imageserver.routes.PostSAMLogoRoute
import net.perfectdreams.imageserver.routes.PostTerminatorAnimeRoute
import net.perfectdreams.imageserver.routes.PostToBeContinuedGeneratorRoute
import net.perfectdreams.imageserver.routes.PostTrumpRoute
import net.perfectdreams.imageserver.routes.cortesflow.CortesFlowRoutes
import net.perfectdreams.imageserver.routes.cortesflow.GetCortesFlowRoute
import net.perfectdreams.imageserver.routes.drake.DrakeRoutes
import net.perfectdreams.imageserver.routes.scaled.ScaledRoutes
import net.perfectdreams.imageserver.routes.skewed.SkewedRoutes
import net.perfectdreams.imageserver.utils.ConnectionManager
import net.perfectdreams.imageserver.utils.Gifsicle
import net.perfectdreams.imageserver.utils.WebsiteAPIException
import net.perfectdreams.imageserver.utils.extensions.alreadyHandledStatus
import net.perfectdreams.imageserver.utils.extensions.respondJson
import java.io.File
import java.util.concurrent.Executors

class GabrielaImageGen(val config: AppConfig) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    val scheduledThreadPool = Executors.newScheduledThreadPool(16)
    val coroutineDispatcher = scheduledThreadPool.asCoroutineDispatcher()

    val generators = Generators(this)
    val gifsicle = Gifsicle(File(config.gifsiclePath))

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
        PostCarlyAaahRoute(this),
        PostPetPetRoute(this),
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
        *SkewedRoutes(this).all().toTypedArray(),
        *ScaledRoutes(this).all().toTypedArray(),
        *DrakeRoutes(this).all().toTypedArray(),
        *CortesFlowRoutes(this).all().toTypedArray()
    )

    fun start() {
        val server = embeddedServer(Netty, port = 8001) {
            install(StatusPages) {
                exception<WebsiteAPIException> { cause ->
                    call.alreadyHandledStatus = true
                    call.respondJson(cause.payload, cause.status)
                }
            }

            routing {
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
    }
}