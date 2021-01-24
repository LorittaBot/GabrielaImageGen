package net.perfectdreams.imagegen.cortesflow

import java.awt.Font
import java.awt.image.BufferedImage

class MiticoSuccCortesFlowGenerator(
    template: BufferedImage,
    font: Font
) : CortesFlowGenerator(
    template,
    font,
    RIGHT_SIDE_TEXT,
    FlowParticipant.MITICO,
    "https://youtu.be/_4FKCxXlu-o"
)