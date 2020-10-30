package net.perfectdreams.imageserver

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import net.perfectdreams.imageserver.config.AppConfig
import java.io.File
import javax.imageio.ImageIO

object GabrielaImageGenLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        // Speeds up image loading/writing/etc
        // Because this is a *global* change, this isn't used on the "gabriela-image-api" module.
        // But it is useful if you are making your own project! :)
        //
        // https://stackoverflow.com/a/44170254/7271796
        ImageIO.setUseCache(false)

        val config = loadConfig<AppConfig>("./app.conf")

        val m = GabrielaImageGen(config)
        m.start()
    }

    inline fun <reified T> loadConfig(path: String): T {
        val lightbendConfig = ConfigFactory.parseFile(File(path))
            .resolve()

        return Hocon.decodeFromConfig(lightbendConfig)
    }
}