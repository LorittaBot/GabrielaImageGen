package net.perfectdreams.imagegen.generators.cortesflow

import java.awt.Font
import java.awt.image.BufferedImage

class IgorPointingCortesFlowGenerator(
    template: BufferedImage,
    font: Font
) : CortesFlowGenerator(
    template,
    font,
    RIGHT_SIDE_TEXT,
    FlowParticipant.IGOR3K,
    "https://youtu.be/pgZ2HIuh1SE"
)