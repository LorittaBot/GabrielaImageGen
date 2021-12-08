package net.perfectdreams.gabrielaimageserver.generators

import net.perfectdreams.gabrielaimageserver.data.MinecraftSkinLorittaSweatshirtRequest
import net.perfectdreams.gabrielaimageserver.exceptions.InvalidMinecraftSkinException
import net.perfectdreams.gabrielaimageserver.generators.utils.ImageUtils.deepCopy
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class MinecraftSkinLorittaSweatshirtGenerator(
    val assets: MinecraftSkinLorittaSweatshirtGeneratorAssets
) : Generator {
    fun generate(source: BufferedImage, sweatshirtStyle: MinecraftSkinLorittaSweatshirtRequest.SweatshirtStyle): BufferedImage {
        // Not a Minecraft skin!
        // 32x64 = pre-1.8
        // 64x64 = 1.8+
        if ((source.height != 64 && source.height != 32) || (source.height != 64 && source.width != 64))
            throw InvalidMinecraftSkinException()

        return createSkin(source, sweatshirtStyle)
    }

    private fun createSkin(
        originalSkin: BufferedImage,
        sweatshirtStyle: MinecraftSkinLorittaSweatshirtRequest.SweatshirtStyle
    ): BufferedImage {
        val isPre18 = originalSkin.height == 32

        val skin = BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB) // Corrige skins paletadas
        val graphics = skin.graphics as Graphics2D
        graphics.drawImage(originalSkin, 0, 0, null)

        if (isPre18) {
            // Converts a skin to 1.8 format
            fun flipAndPaste(bufferedImage: BufferedImage, x: Int, y: Int) {
                graphics.drawImage(bufferedImage, x + bufferedImage.width, y, -bufferedImage.width, bufferedImage.height, null)
            }

            // i have no idea what I'm doing
            val leg0 = skin.getSubimage(0, 16, 16, 4).clone()
            val leg1 = skin.getSubimage(4, 20, 8, 12).clone()
            val leg2 = skin.getSubimage(0, 20, 4, 12).clone()
            val leg3 = skin.getSubimage(12, 20, 4, 12).clone()

            val arm0 = skin.getSubimage(40, 16, 16, 4).clone()
            val arm1 = skin.getSubimage(4 + 40, 20, 8, 12).clone()
            val arm2 = skin.getSubimage(0 + 40, 20, 4, 12).clone()
            val arm3 = skin.getSubimage(12 + 40, 20, 4, 12).clone()

            graphics.drawImage(leg0, 16, 48, null)
            flipAndPaste(leg1, 16, 52)
            flipAndPaste(leg2, 24, 52)
            flipAndPaste(leg3, 28, 52)

            graphics.drawImage(arm0, 32, 48, null)
            flipAndPaste(arm1, 32, 52)
            flipAndPaste(arm2, 40, 52)
            flipAndPaste(arm3, 44, 52)
        }

        val alexTestColor = Color(skin.getRGB(50, 16), true)
        val isAlex = alexTestColor.alpha != 255

        val template = when (sweatshirtStyle) {
            MinecraftSkinLorittaSweatshirtRequest.SweatshirtStyle.LIGHT -> if (isAlex) assets.slimLight else assets.classicLight
            MinecraftSkinLorittaSweatshirtRequest.SweatshirtStyle.DARK -> if (isAlex) assets.slimDark else assets.classicDark
            MinecraftSkinLorittaSweatshirtRequest.SweatshirtStyle.MIX_WAVY -> if (isAlex) assets.slimMixWavy else assets.classicMixWavy
            MinecraftSkinLorittaSweatshirtRequest.SweatshirtStyle.MIX_WAVY_WITH_STITCHES -> if (isAlex) assets.slimMixWavyWithStitches else assets.classicMixWavyWithStitches
            MinecraftSkinLorittaSweatshirtRequest.SweatshirtStyle.MIX_VERTICAL -> if (isAlex) assets.slimMixVertical else assets.classicMixVertical
            MinecraftSkinLorittaSweatshirtRequest.SweatshirtStyle.MIX_VERTICAL_WITH_STITCHES -> if (isAlex) assets.slimMixVerticalWithStitches else assets.classicMixVerticalWithStitches
        }

        // This does... something?
        // Sorry, I'm not really sure what this is for, I tried remembering what it does but I just *can't* figure out
        // it seems it tries to fix the skin's hand for some reason...?
        val handColor = if (isAlex) {
            Color(skin.getRGB(48, 17), true)
        } else {
            Color(skin.getRGB(49, 17), true)
        }

        var wrong = 0
        for (x in 40..43) {
            val color = Color(skin.getRGB(x, 31))
            if (handColor.red in color.red - 40 until color.red + 40) {
                if (handColor.green in color.green - 40 until color.green + 40) {
                    if (handColor.blue in color.blue - 40 until color.blue + 40) {
                        continue
                    }
                }
            }

            wrong++
        }
        val lowerBarWrong = wrong > 2

        for (x in 40..43) {
            val color = Color(skin.getRGB(x, 30))
            if (handColor.red in color.red - 40 until color.red + 40) {
                if (handColor.green in color.green - 40 until color.green + 40) {
                    if (handColor.blue in color.blue - 40 until color.blue + 40) {
                        continue
                    }
                }
            }

            wrong++
        }

        if (wrong > 2) {
            graphics.color = handColor
            val arm1 = deepCopy(skin.getSubimage(40, 31, if (isAlex) 14 else 16, 1))
            val arm2 = deepCopy(skin.getSubimage(32, 63, if (isAlex) 14 else 16, 1))

            // ARMS
            graphics.fillRect(40, 30, if (isAlex) 14 else 16, if (!lowerBarWrong) 1 else 2)
            graphics.fillRect(32, 62, if (isAlex) 14 else 16, if (!lowerBarWrong) 1 else 2)

            // HANDS
            if (lowerBarWrong) {
                graphics.fillRect(if (isAlex) 47 else 48, 16, if (isAlex) 3 else 4, 4)
                graphics.fillRect(if (isAlex) 39 else 40, 48, if (isAlex) 3 else 4, 4)
            } else {
                // println("Fixing arm by copying lower pixels")
                graphics.drawImage(arm1, 40, 30, null)
                graphics.drawImage(arm2, 32, 62, null)
            }
        }

        graphics.background = Color(255, 255, 255, 0)
        graphics.clearRect(16, 32, 48, 16)
        graphics.clearRect(48, 48, 16, 16)
        graphics.drawImage(template, 0, 0, null)

        return skin
    }

    data class MinecraftSkinLorittaSweatshirtGeneratorAssets(
        val classicLight: BufferedImage,
        val slimLight: BufferedImage,

        val classicDark: BufferedImage,
        val slimDark: BufferedImage,

        val classicMixWavy: BufferedImage,
        val slimMixWavy: BufferedImage,

        val classicMixWavyWithStitches: BufferedImage,
        val slimMixWavyWithStitches: BufferedImage,

        val classicMixVertical: BufferedImage,
        val slimMixVertical: BufferedImage,

        val classicMixVerticalWithStitches: BufferedImage,
        val slimMixVerticalWithStitches: BufferedImage
    )
}