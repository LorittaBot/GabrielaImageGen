package net.perfectdreams.gabrielaimageserver.data

import kotlinx.serialization.Serializable

@Serializable
data class FansExplainingRequest(
    val section1Line1: String,
    val section1Line2: String,

    val section2Line1: String,
    val section2Line2: String,

    val section3Line1: String,
    val section3Line2: String,

    val section4Line1: String,
    val section4Line2: String,

    val section5Line1: String,
    val section5Line2: String
)