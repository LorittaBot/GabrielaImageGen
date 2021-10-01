package net.perfectdreams.gabrielaimageserver.webserver

import com.typesafe.config.ConfigFactory
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import net.perfectdreams.gabrielaimageserver.webserver.config.AppConfig
import net.perfectdreams.gabrielaimageserver.webserver.utils.Prometheus
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

        val configurationFile = File(System.getProperty("conf") ?: "app.conf")

        if (!configurationFile.exists()) {
            println("Welcome to Gabriela's Image Generation Server! :3")
            println("")
            println("Before we start, you will need to configure me.")
            println("I will create a file named \"app.conf\", open it on your favorite text editor and change it!")
            println("")
            println("After configuring the file, run me again!")

            copyFromJar("/app.conf", "app.conf")

            System.exit(1)
            return
        }

        val config = loadConfig<AppConfig>("app.conf")

        Prometheus.register()

        val m = GabrielaImageGen(config)
        m.start()
    }

    private fun copyFromJar(inputPath: String, outputPath: String) {
        val inputStream = GabrielaImageGenLauncher::class.java.getResourceAsStream(inputPath)
        File(outputPath).writeBytes(inputStream.readAllBytes())
    }

    inline fun <reified T> loadConfig(path: String): T {
        val lightbendConfig = ConfigFactory.parseFile(File(path))
            .resolve()

        return Hocon.decodeFromConfig(lightbendConfig)
    }
}