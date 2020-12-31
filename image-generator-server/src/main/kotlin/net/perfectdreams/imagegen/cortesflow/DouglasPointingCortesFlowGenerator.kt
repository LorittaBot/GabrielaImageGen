package net.perfectdreams.imagegen.cortesflow

import java.awt.Font
import java.awt.image.BufferedImage

class DouglasPointingCortesFlowGenerator(
    template: BufferedImage,
    font: Font
) : CortesFlowGenerator(
    template,
    font,
    RIGHT_SIDE_TEXT,
    FlowParticipant.DOUGLAS,
    "https://youtu.be/Nz-BY4o_mLk"
)