package net.perfectdreams.imagegen.cortesflow

import java.awt.Font
import java.awt.image.BufferedImage

class MonarkDiscussionCortesFlowGenerator(
    template: BufferedImage,
    font: Font
) : CortesFlowGenerator(
    template,
    font,
    LEFT_SIDE_TEXT,
    FlowParticipant.MONARK,
    "https://youtu.be/vVrdmg1-C3U"
)