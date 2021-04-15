package net.perfectdreams.imageserver.utils

import net.perfectdreams.imagegen.graphics.LorittaImage
import net.perfectdreams.imageserver.utils.extensions.toBufferedImage
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    val skin = ImageIO.read(File("L:\\Pictures\\Loritta_Skin.png"))

    val steve = ImageIO.read(File("L:\\Pictures\\f0a24c60e5b899f27b4bca0e1efd36ef.png"))

    val steveGraphics = steve.createGraphics()

    val headSkin = skin.getSubimage(8, 8, 8, 8)
            .getScaledInstance(steve.width, steve.height, BufferedImage.SCALE_FAST)
            .toBufferedImage()

    val overlayHeadSkin = skin.getSubimage(40, 8, 8, 8)
            .getScaledInstance(steve.width, steve.height, BufferedImage.SCALE_FAST)
            .toBufferedImage()

    steveGraphics.drawImage(
            LorittaImage(headSkin)
                    .apply {
                        setCorners(
                            183f, 39f,
                            332f, 1f,
                            333f, 184f,
                            185f, 201f
                        )
                    }.bufferedImage,
            0,
            0,
            null
    )

    steveGraphics.drawImage(
            LorittaImage(overlayHeadSkin)
                    .apply {
                        setCorners(
                                183f, 39f,
                                332f, 1f,
                                333f, 184f,
                                185f, 201f
                        )
                    }.bufferedImage,
            0,
            0,
            null
    )

    ImageIO.write(
            steve,
            "png",
            File("L:\\Pictures\\lori_steve_pose.png")
    )
}