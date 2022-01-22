package net.perfectdreams.gabrielaimageserver.generators

import net.perfectdreams.gabrielaimageserver.generators.utils.toBufferedImage
import net.perfectdreams.gabrielaimageserver.graphics.LorittaImage
import java.awt.geom.Path2D
import java.awt.image.BufferedImage

class DrawnMaskSignGenerator(
    val image: BufferedImage
) : SingleSourceImageToImageGenerator {
    override fun generateImage(source: BufferedImage): BufferedImage {
        val base = BufferedImage(405, 550, BufferedImage.TYPE_INT_ARGB)
        val scaled = source.getScaledInstance(405, 550, BufferedImage.SCALE_SMOOTH).toBufferedImage()

        val transformed = LorittaImage(scaled)
        transformed.setCorners(167f, 320f,
            367f, 302f,
            387f, 410f,
            179f, 436f)

        val transformedSignImage = transformed.bufferedImage
        val clippedSignImage = BufferedImage(405, 550, BufferedImage.TYPE_INT_ARGB)
        val clippedSignGraphics = clippedSignImage.graphics

        // Para ficar a imagem perfeitamente na mão do Luca, vamos fazer uns snip snip nela!
        val path = Path2D.Double()
        path.moveTo(167.0, 320.0)
        path.lineTo(355.0, 303.0)
        path.lineTo(364.0, 306.0)
        path.lineTo(369.0, 319.0)
        path.lineTo(386.0, 401.0)
        path.lineTo(386.0, 404.0)
        path.lineTo(383.0, 408.0)
        path.lineTo(377.0, 411.0)
        path.lineTo(177.0, 437.0)
        path.closePath()

        clippedSignGraphics.clip = path
        clippedSignGraphics.drawImage(transformedSignImage, 0, 0, null)
        base.graphics.drawImage(clippedSignImage, 0, 0, null)
        base.graphics.drawImage(image, 0, 0, null)

        return base
    }
}