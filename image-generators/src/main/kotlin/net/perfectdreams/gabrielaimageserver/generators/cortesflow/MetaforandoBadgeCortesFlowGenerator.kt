package net.perfectdreams.gabrielaimageserver.generators.cortesflow

import java.awt.Font
import java.awt.image.BufferedImage

class MetaforandoBadgeCortesFlowGenerator(
    template: BufferedImage,
    font: Font
) : CortesFlowGenerator(
    template,
    font,
    RIGHT_SIDE_TEXT,
    FlowParticipant.METAFORANDO,
    "https://youtu.be/lGartyYTp64"
)