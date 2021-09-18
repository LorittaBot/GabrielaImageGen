package net.perfectdreams.imagegen.generators.undertale.textbox

import java.awt.Image
import java.awt.image.BufferedImage

sealed class CharacterPortrait(val image: Image) {
    companion object {
        // Width and height!
        private const val MAX_PORTRAIT_SIZE = 110

        fun fromGame(bufferedImage: BufferedImage) = TobyGameCharacterPortrait(
            bufferedImage.getScaledInstance(bufferedImage.width * 2, bufferedImage.height * 2, BufferedImage.SCALE_FAST)
        )

        fun fromCustom(bufferedImage: BufferedImage): CustomImageCharacterPortrait {
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

            return CustomImageCharacterPortrait(
                bufferedImage.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH)
            )
        }
    }
}

class TobyGameCharacterPortrait(image: Image) : CharacterPortrait(image)
class CustomImageCharacterPortrait(image: Image) : CharacterPortrait(image)