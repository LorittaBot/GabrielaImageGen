package net.perfectdreams.imageserver

import javax.imageio.ImageIO

object GabrielaImageGenLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val m = GabrielaImageGen()
        m.start()
    }
}