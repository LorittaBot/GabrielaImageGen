package net.perfectdreams.imagegen.generators.cortesflow

import java.awt.Font
import java.awt.image.BufferedImage

class PoladofulDiscussionCortesFlowGenerator(
    template: BufferedImage,
    font: Font
) : CortesFlowGenerator(
    template,
    font,
    RIGHT_SIDE_TEXT,
    FlowParticipant.POLADOFUL,
    "https://youtu.be/sk8ckvv5Vpw"
)