package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class SAMLogoRequest(
    val image: SourceImageData,
    val type: LogoType
) {
    enum class LogoType {
        SAM_1,
        SAM_2,
        SAM_3
    }
}