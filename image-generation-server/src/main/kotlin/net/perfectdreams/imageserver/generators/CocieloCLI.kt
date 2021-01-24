package net.perfectdreams.imageserver.generators

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import java.io.File
import java.net.URL
import javax.imageio.ImageIO
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

fun main() {
    // readLine()

    ImageIO.setUseCache(false)

    val generator = CocieloChavesGenerator(
            File("L:\\LorittaAssets\\chaves\\temp"),
            File("L:\\LorittaAssets\\chaves"),
            File("L:\\Tools\\ffmpeg\\ffmpeg.exe")
    )

    val templateCocielo = ImageIO.read(
            // File("L:\\Pictures\\500766264377999360.png")
            // URL("https://cdn.discordapp.com/emojis/712660270232633355.png?v=1")
            // File("L:\\Pictures\\Emojis\\gesso.png")
            // File("L:\\Pictures\\cat_nya.png")
            // URL("https://pbs.twimg.com/profile_images/1344969400005386240/74Y0Rsyw_400x400.jpg")
            URL("https://cdn.discordapp.com/avatars/297153970613387264/c1538b78f6660a48d177a7c4ec129b96.png?size=256")
    )

    val templateIgao = ImageIO.read(
            // File("L:\\Pictures\\Emojis\\gesso.png")
            // URL("https://cdn.discordapp.com/emojis/791017821194551346.png?v=1"),
            URL("https://cdn.discordapp.com/avatars/213466096718708737/84b83a87f8e7a1475f989cbbd76c48d8.png?size=256")
    )

    val templateTobiasFunk = ImageIO.read(
            // File("L:\\Pictures\\Emojis\\tobias_funk.png")
            URL("https://cdn.discordapp.com/avatars/270904126974590976/d60c6bd5971f06776ba96497117f7f58.png?size=256")
    )

    val templateDokyoFunkeiro = ImageIO.read(
            // File("L:\\Pictures\\dokyo_funkeiro.png")
            URL("https://cdn.discordapp.com/avatars/159985870458322944/b50adff099924dd5e6b72d13f77eb9d7.png?size=256")
    )

    val templateKekFunk = ImageIO.read(
            // File("L:\\Pictures\\kek_funk.png")
            URL("https://cdn.discordapp.com/avatars/234395307759108106/0e7adc5d634d957b7725021c067bfd87.png?size=256")
    )

    val semaphore = Semaphore(4)

    runBlocking {
        val j = mutableListOf<Deferred<*>>()

        val t = measureTimeMillis {
            repeat(16) {
                val result = generator.generate(
                        templateCocielo,
                        templateIgao,
                        templateTobiasFunk,
                        templateDokyoFunkeiro,
                        templateKekFunk,
                        false
                )

                /* File("L:\\LorittaAssets\\chaves\\result_$it.mp4")
                        .writeBytes(result) */
            }
        }

        println("Took: ${t}ms")
    }
}