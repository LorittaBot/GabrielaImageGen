package net.perfectdreams.gabrielaimageserver.client.services

import net.perfectdreams.gabrielaimageserver.client.GabrielaImageServerClient
import net.perfectdreams.gabrielaimageserver.data.CocieloChavesRequest
import net.perfectdreams.gabrielaimageserver.data.FansExplainingRequest
import net.perfectdreams.gabrielaimageserver.data.GigaChadRequest
import net.perfectdreams.gabrielaimageserver.data.SingleImageRequest

class VideosService(client: GabrielaImageServerClient) : Service(client) {
    suspend fun attackOnHeart(request: SingleImageRequest) = execute("/videos/attack-on-heart", request)
    suspend fun carlyAaah(request: SingleImageRequest) = execute("/videos/carly-aaah", request)
    suspend fun fansExplaining(request: FansExplainingRequest) = execute("/videos/fans-explaining", request)
    suspend fun cocieloChaves(request: CocieloChavesRequest) = execute("/videos/cocielo-chaves", request)
    suspend fun gigaChad(request: GigaChadRequest) = execute("/videos/gigachad", request)
}