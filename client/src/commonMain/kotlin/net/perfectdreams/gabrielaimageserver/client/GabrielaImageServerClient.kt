package net.perfectdreams.gabrielaimageserver.client

import io.ktor.client.*
import net.perfectdreams.gabrielaimageserver.client.services.ImagesService
import net.perfectdreams.gabrielaimageserver.client.services.VideosService

/**
 * Client for [GabrielaImageServer](https://github.com/LorittaBot/GabrielaImageGen)
 *
 * While the requests themselves are very simple, this is useful to wrap errors and exceptions.
 *
 * @param baseUrl the URL of the image server, example: https://gabriela.loritta.website
 * @param http    the http client that will be used for requests
 */
class GabrielaImageServerClient(baseUrl: String, val http: HttpClient) {
    val baseUrl = baseUrl.removeSuffix("/") // Remove trailing slash

    val images = ImagesService(this)
    val videos = VideosService(this)
}