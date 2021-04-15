package net.perfectdreams.imagegen.generators.cortesflow

import java.awt.Font
import java.awt.image.BufferedImage

class DouglasPrayCortesFlowGenerator(
    template: BufferedImage,
    font: Font
) : CortesFlowGenerator(
    template,
    font,
    RIGHT_SIDE_TEXT,
    FlowParticipant.DOUGLAS,
    "https://youtu.be/wd_nqyMZexQ"
)