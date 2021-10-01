package net.perfectdreams.gabrielaimageserver.generators.undertale.textbox

import net.perfectdreams.gabrielaimageserver.data.TobyTextBoxRequest
import java.awt.Image
import java.awt.image.BufferedImage

sealed class CharacterPortrait(
    val image: Image
) {
    companion object {
        // Width and height!
        private const val MAX_PORTRAIT_SIZE = 110

        fun fromGame(
            bufferedImage: BufferedImage,
            centerX: Int,
            centerY: Int
        ) = TobyGameCharacterPortrait(
            bufferedImage.getScaledInstance(bufferedImage.width * 2, bufferedImage.height * 2, BufferedImage.SCALE_FAST),
            centerX,
            centerY
        )

        fun fromCustom(bufferedImage: BufferedImage, colorPortraitType: TobyTextBoxRequest.ColorPortraitType): CustomImageCharacterPortrait {
            // Time to scale the input to match the size!
            // We need to maintain the aspect ratio too

            // RULE OF THREE!!11!
            val newWidth: Int
            val newHeight: Int

            if (bufferedImage.width > bufferedImage.height) {
                // larguraOriginal - larguraDoContextImage
                // alturaOriginal - X
                newWidth = MAX_PORTRAIT_SIZE
                newHeight = (bufferedImage.height * MAX_PORTRAIT_SIZE) / bufferedImage.width
            } else {
                newWidth = (bufferedImage.width * MAX_PORTRAIT_SIZE) / bufferedImage.height
                newHeight = MAX_PORTRAIT_SIZE
            }

            val scaledInstance = bufferedImage.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH)

            val finalImage = when (colorPortraitType) {
                TobyTextBoxRequest.ColorPortraitType.COLORED -> scaledInstance
                TobyTextBoxRequest.ColorPortraitType.BLACK_AND_WHITE -> {
                    BufferedImage(
                        scaledInstance.getWidth(null),
                        scaledInstance.getHeight(null),
                        BufferedImage.TYPE_BYTE_BINARY
                    ).apply {
                        createGraphics().drawImage(scaledInstance, 0, 0, null)
                    }
                }
                TobyTextBoxRequest.ColorPortraitType.SHADES_OF_GRAY -> {
                    BufferedImage(
                        scaledInstance.getWidth(null),
                        scaledInstance.getHeight(null),
                        BufferedImage.TYPE_BYTE_GRAY
                    ).apply {
                        createGraphics().drawImage(scaledInstance, 0, 0, null)
                    }
                }
            }

            return CustomImageCharacterPortrait(finalImage)
        }
    }
}

class TobyGameCharacterPortrait(
    image: Image,
    val centerX: Int? = null,
    val centerY: Int? = null
) : CharacterPortrait(image)
class CustomImageCharacterPortrait(image: Image) : CharacterPortrait(image)