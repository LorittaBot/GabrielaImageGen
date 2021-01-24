import com.typesafe.config.ConfigFactory
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import net.perfectdreams.imageserver.GabrielaImageGenLauncher
import net.perfectdreams.imageserver.config.AppConfig
import org.junit.jupiter.api.Test

class CheckDefaultConfigTest {
    private fun loadFromJar(inputPath: String): String {
        val inputStream = GabrielaImageGenLauncher::class.java.getResourceAsStream(inputPath)
        return inputStream.bufferedReader(Charsets.UTF_8).readText()
    }

    @Test
    fun `check general config`() {
        val configurationFile = loadFromJar("/app.conf")
        Hocon.decodeFromConfig<AppConfig>(
            ConfigFactory.parseString(configurationFile)
                .resolve()
        )
    }
}