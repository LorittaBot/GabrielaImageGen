package net.perfectdreams.gabrielaimageserver.generators.cortesflow

import java.awt.Font
import java.awt.image.BufferedImage

class MonarkSmokingCortesFlowGenerator(
    template: BufferedImage,
    font: Font
) : CortesFlowGenerator(
    template,
    font,
    RIGHT_SIDE_TEXT,
    FlowParticipant.MONARK,
    "https://youtu.be/08aJ1BnjQKY"
)