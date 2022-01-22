package net.perfectdreams.gabrielaimageserver.client.services

import net.perfectdreams.gabrielaimageserver.client.GabrielaImageServerClient
import net.perfectdreams.gabrielaimageserver.data.CortesFlowRequest
import net.perfectdreams.gabrielaimageserver.data.DrawnMaskAtendenteRequest
import net.perfectdreams.gabrielaimageserver.data.DrawnMaskWordRequest
import net.perfectdreams.gabrielaimageserver.data.ManiaTitleCardRequest
import net.perfectdreams.gabrielaimageserver.data.MemeMakerRequest
import net.perfectdreams.gabrielaimageserver.data.MinecraftSkinLorittaSweatshirtRequest
import net.perfectdreams.gabrielaimageserver.data.PetPetRequest
import net.perfectdreams.gabrielaimageserver.data.SAMLogoRequest
import net.perfectdreams.gabrielaimageserver.data.ShipRequest
import net.perfectdreams.gabrielaimageserver.data.SingleImageRequest
import net.perfectdreams.gabrielaimageserver.data.TerminatorAnimeRequest
import net.perfectdreams.gabrielaimageserver.data.TobyTextBoxRequest
import net.perfectdreams.gabrielaimageserver.data.TwoImagesRequest

class ImagesService(client: GabrielaImageServerClient) : Service(client) {
    suspend fun ship(request: ShipRequest) = execute("/images/ship", request)
    suspend fun petPet(request: PetPetRequest) = execute("/images/pet-pet", request)
    suspend fun maniaTitleCard(request: ManiaTitleCardRequest) = execute("/images/mania-title-card", request)
    suspend fun tobyTextBox(request: TobyTextBoxRequest) = execute("/images/toby-text-box", request)
    suspend fun memeMaker(request: MemeMakerRequest) = execute("/images/meme-maker", request)
    suspend fun minecraftSkinLorittaSweatshirt(request: MinecraftSkinLorittaSweatshirtRequest) = execute("/images/minecraft-skin-loritta-sweatshirt", request)
    suspend fun drawnMaskAtendente(request: DrawnMaskAtendenteRequest) = execute("/images/drawn-mask-atendente", request)
    suspend fun drawnMaskWord(request: DrawnMaskWordRequest) = execute("/images/drawn-mask-word", request)

    suspend fun invertColors(request: SingleImageRequest) = execute("/images/invert-colors", request)
    suspend fun toBeContinued(request: SingleImageRequest) = execute("/images/to-be-continued", request)
    suspend fun cepoDeMadeira(request: SingleImageRequest) = execute("/images/cepo-de-madeira", request)
    suspend fun knucklesThrow(request: SingleImageRequest) = execute("/images/knuckles-throw", request)
    suspend fun nichijouYuukoPaper(request: SingleImageRequest) = execute("/images/nichijou-yuuko-paper", request)
    suspend fun getOverHere(request: SingleImageRequest) = execute("/images/get-over-here", request)
    suspend fun drawnMaskSign(request: SingleImageRequest) = execute("/images/drawn-mask-sign", request)
    suspend fun drake(request: TwoImagesRequest) = execute("/images/drake", request)
    suspend fun bolsoDrake(request: TwoImagesRequest) = execute("/images/bolso-drake", request)
    suspend fun loriDrake(request: TwoImagesRequest) = execute("/images/lori-drake", request)
    suspend fun trump(request: TwoImagesRequest) = execute("/images/trump", request)
    suspend fun samLogo(request: SAMLogoRequest) = execute("/images/sam", request)
    suspend fun terminatorAnime(request: TerminatorAnimeRequest) = execute("/images/terminator-anime", request)
    suspend fun cortesFlow(path: String, request: CortesFlowRequest) = execute("/images/cortes-flow/$path", request)

    // ===[ SCALED IMAGES (with one single image) GENERATORS ]===
    suspend fun pepeDream(request: SingleImageRequest) = execute("/images/pepe-dream", request)
    suspend fun loriScared(request: SingleImageRequest) = execute("/images/lori-scared", request)
    suspend fun studiopolisTv(request: SingleImageRequest) = execute("/images/studiopolis-tv", request)
    suspend fun markMeta(request: SingleImageRequest) = execute("/images/mark-meta", request)

    // ===[ SKEWED IMAGES (with one single image) GENERATORS ]===
    suspend fun art(request: SingleImageRequest) = execute("/images/art", request)
    suspend fun bobBurningPaper(request: SingleImageRequest) = execute("/images/bob-burning-paper", request)
    suspend fun bolsoFrame(request: SingleImageRequest) = execute("/images/bolso-frame", request)
    suspend fun bolsonaro(request: SingleImageRequest) = execute("/images/bolsonaro", request)
    suspend fun bolsonaro2(request: SingleImageRequest) = execute("/images/bolsonaro2", request)
    suspend fun briggsCover(request: SingleImageRequest) = execute("/images/briggs-cover", request)
    suspend fun buckShirt(request: SingleImageRequest) = execute("/images/buck-shirt", request)
    suspend fun canellaDvd(request: SingleImageRequest) = execute("/images/canella-dvd", request)
    suspend fun chicoAta(request: SingleImageRequest) = execute("/images/chico-ata", request)
    suspend fun ednaldoBandeira(request: SingleImageRequest) = execute("/images/ednaldo-bandeira", request)
    suspend fun ednaldoTv(request: SingleImageRequest) = execute("/images/ednaldo-tv", request)
    suspend fun gessyAta(request: SingleImageRequest) = execute("/images/gessy-ata", request)
    suspend fun loriAta(request: SingleImageRequest) = execute("/images/lori-ata", request)
    suspend fun loriSign(request: SingleImageRequest) = execute("/images/lori-sign", request)
    suspend fun monicaAta(request: SingleImageRequest) = execute("/images/monica-ata", request)
    suspend fun passingPaper(request: SingleImageRequest) = execute("/images/passing-paper", request)
    suspend fun romeroBritto(request: SingleImageRequest) = execute("/images/romero-britto", request)
    suspend fun wolverineFrame(request: SingleImageRequest) = execute("/images/wolverine-frame", request)
    suspend fun ripTv(request: SingleImageRequest) = execute("/images/rip-tv", request)
}