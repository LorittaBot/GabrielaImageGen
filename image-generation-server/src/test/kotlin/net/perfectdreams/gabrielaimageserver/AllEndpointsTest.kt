package net.perfectdreams.gabrielaimageserver
import io.ktor.client.*
import io.ktor.client.features.*
import kotlinx.coroutines.runBlocking
import net.perfectdreams.gabrielaimageserver.client.GabrielaImageServerClient
import net.perfectdreams.gabrielaimageserver.client.services.ImagesService
import net.perfectdreams.gabrielaimageserver.client.services.VideosService
import net.perfectdreams.gabrielaimageserver.data.CocieloChavesRequest
import net.perfectdreams.gabrielaimageserver.data.CortesFlowRequest
import net.perfectdreams.gabrielaimageserver.data.FansExplainingRequest
import net.perfectdreams.gabrielaimageserver.data.ManiaTitleCardRequest
import net.perfectdreams.gabrielaimageserver.data.MemeMakerRequest
import net.perfectdreams.gabrielaimageserver.data.PetPetRequest
import net.perfectdreams.gabrielaimageserver.data.SAMLogoRequest
import net.perfectdreams.gabrielaimageserver.data.ShipRequest
import net.perfectdreams.gabrielaimageserver.data.SingleImageRequest
import net.perfectdreams.gabrielaimageserver.data.TerminatorAnimeRequest
import net.perfectdreams.gabrielaimageserver.data.TobyTextBoxRequest
import net.perfectdreams.gabrielaimageserver.data.TwoImagesRequest
import net.perfectdreams.gabrielaimageserver.data.URLImageData
import org.junit.jupiter.api.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import kotlin.reflect.full.callSuspend
import kotlin.reflect.jvm.jvmErasure

/**
 * This actually tests two things:
 * * Tests if the Image Generation Server Docker container works
 * * If the endpoints are configured correctly
 */
@Testcontainers
class AllEndpointsTest {
    @Container
    var simpleWebServer = GenericContainer<Nothing>("ghcr.io/lorittabot/gabriela-image-server:latest").apply {
        withExposedPorts(8001)
    }

    private val dontExecuteMethods = listOf(
        "hashCode",
        "toString",
        "execute",
        "equals"
    )

    private val urlData = URLImageData(
        "https://cdn.discordapp.com/emojis/585536267530534913.png?v=1"
    )

    @Test
    fun `test all endpoints`() = runBlocking<Unit> {
        val address = "http://" + simpleWebServer.containerIpAddress + ":" + simpleWebServer.getMappedPort(8001)

        HttpClient {
            expectSuccess = false

            install(HttpTimeout) {
                // If it ends up taking more than 60s, then something is *very* wrong
                socketTimeoutMillis = 60_000
                connectTimeoutMillis = 60_000
                requestTimeoutMillis = 60_000
            }
        }.use {
            val client = GabrielaImageServerClient(address, it)

            val types = listOf(
                client.images,
                client.videos,
                SingleImageRequest(urlData),
                TwoImagesRequest(urlData, urlData),
                ShipRequest(urlData, urlData, 99),
                PetPetRequest(urlData, squish = 2.0, delayBetweenFrames = 4),
                ManiaTitleCardRequest("god dammit kris", "where we are"),
                TobyTextBoxRequest("god dammit kris where the fuck are we", TobyTextBoxRequest.TextBoxType.ORIGINAL, "deltarune/susie/neutral", null, null),
                FansExplainingRequest("a", "b", "c", "d", "e", "f", "g", "i", "j", "k"),
                CocieloChavesRequest(urlData, urlData, urlData, urlData, urlData),
                MemeMakerRequest(urlData, "oh no", "look at the sad cat"),
                SAMLogoRequest(urlData, SAMLogoRequest.LogoType.SAM_3),
                TerminatorAnimeRequest("haha imma kill u", "nooo go away :sob:"),
                CortesFlowRequest("discussão owo")
            )

            val membersToBeChecked = ImagesService::class.members + VideosService::class.members
            member@for (member in membersToBeChecked) {
                println(member)
                println("Size: ${member.parameters.size}")

                if (member.name in dontExecuteMethods)
                    continue

                val arguments = mutableListOf<Any>()

                paramLoop@for (parameter in member.parameters) {
                    for (type in types) {
                        if (parameter.type.jvmErasure.java == String::class && member.name == "cortesFlow") {
                            arguments.add("monark-discussion")
                            continue@paramLoop
                        }
                        if (parameter.type.jvmErasure.java == type::class.java) {
                            arguments.add(type)
                            continue@paramLoop
                        }
                    }

                    // Unknown parameter
                    println("Didn't test $member due to the lack of a ${parameter.type.jvmErasure.java} instance...")
                    continue@member
                }

                if (arguments.size != member.parameters.size) {
                    println("Didn't test $member due to the lack of enough parameters...")
                    continue@member
                }

                if (arguments.isEmpty()) {
                    println("Didn't test $member because we didn't even add our own arguments to it...")
                    continue@member
                }

                // Call API!!!
                println("Executing $member...")
                val response = member.callSuspend(
                    *arguments.toTypedArray()
                )

                File("L:\\RandomProjects\\GabrielaImageGen\\${member.name}.png")
                    .writeBytes(response as ByteArray)
                println("Executed $member!")
            }
        }
    }
}