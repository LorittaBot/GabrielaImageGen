package net.perfectdreams.imageserver.config

import kotlinx.serialization.Serializable

@Serializable
class AppConfig(
    val tempFolder: String,
    val assetsFolder: String,
    val ffmpegPath: String,
    val gifsiclePath: String
)