package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class SadRealityRequest(
    val user1: SadRealityUser,
    val user2: SadRealityUser,
    val user3: SadRealityUser,
    val user4: SadRealityUser,
    val user5: SadRealityUser,
    val user6: SadRealityUser
) {
    @Serializable
    data class SadRealityUser(
        val text: String,
        val name: String?,
        val image: SourceImageData,
    )
}