package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.generators.undertale.textbox.CharacterPortrait
import net.perfectdreams.imagegen.generators.undertale.textbox.TobyGameCharacterPortrait
import net.perfectdreams.imagegen.generators.undertale.textbox.UndertaleBoxGenerator
import net.perfectdreams.imageserver.generators.Generators
import java.awt.Font
import java.io.File
import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

class TobyTextBoxGenerators(val m: Generators) {
    val portraits = mutableMapOf<String, TobyGameCharacterPortrait>()

    init {
        val uri = TobyTextBoxGenerators::class.java.getResource("/image_templates/undertale/textbox/characters/").toURI()
        val dirPath = try {
            Paths.get(uri)
        } catch (e: FileSystemNotFoundException) {
            // If this is thrown, then it means that we are running the JAR directly (example: not from an IDE)
            val env = mutableMapOf<String, String>()
            FileSystems.newFileSystem(uri, env).getPath("/image_templates/undertale/textbox/characters/")
        }

        Files.list(dirPath).filter { Files.isDirectory(it) }.forEach { directoryPath ->
            Files.list(directoryPath).forEach { filePath ->
                if (filePath.fileName.toString().endsWith(".png")) {
                    portraits["${directoryPath.fileName}/${filePath.fileName.toString().substringBeforeLast(".")}"] = CharacterPortrait.fromGame(
                        ImageIO.read(Files.newInputStream(filePath)))
                }
            }
        }
    }

    private val undertaleDialogFont = Font.createFont(
        Font.TRUETYPE_FONT,
        File(m.m.config.assetsFolder + "/fonts/dtm-mono.otf")
    )

    val undertaleTextBoxGenerator = UndertaleBoxGenerator(
        m.loadBufferedImage("image_templates/undertale/textbox/original.png"),
        undertaleDialogFont
    )

    val darkWorldTextBoxGenerator = UndertaleBoxGenerator(
        m.loadBufferedImage("image_templates/undertale/textbox/dark_world.png"),
        undertaleDialogFont
    )
}