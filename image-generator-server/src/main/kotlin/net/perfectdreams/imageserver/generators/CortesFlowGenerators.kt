package net.perfectdreams.imageserver.generators

import net.perfectdreams.imagegen.cortesflow.*
import net.perfectdreams.imageserver.GabrielaImageGen
import net.perfectdreams.imageserver.convertToSnakeCase
import java.awt.Font
import javax.imageio.ImageIO

class CortesFlowGenerators {
    val montserratExtraBold = Font.createFont(
            Font.TRUETYPE_FONT,
            GabrielaImageGen::class.java.getResourceAsStream("/image_templates/cortes_flow/montserrat-extrabold.otf")
    )

    // ===[ CORTES FLOW ]===
    // ===[ MONARK ]===
    val monarkDiscussion = createCortesFlowGenerator<MonarkDiscussionCortesFlowGenerator>()
    val monarkSmoking = createCortesFlowGenerator<MonarkSmokingCortesFlowGenerator>()
    val monarkStop = createCortesFlowGenerator<MonarkStopCortesFlowGenerator>()

    // ===[ IGOR3k ]===
    val igorAngry = createCortesFlowGenerator<IgorAngryCortesFlowGenerator>()
    val igorPointing = createCortesFlowGenerator<IgorPointingCortesFlowGenerator>()
    val igorNaked = createCortesFlowGenerator<IgorNakedCortesFlowGenerator>()

    // ===[ GAULES ]===
    val gaulesSad = createCortesFlowGenerator<GaulesSadCortesFlowGenerator>()

    // ===[ MÍTICO ]===
    val miticoSucc = createCortesFlowGenerator<MiticoSuccCortesFlowGenerator>()

    // ===[ LUCAS INUTILISMO ]===
    val lucasInutilismoExalted = createCortesFlowGenerator<LucasInutilismoExaltedCortesFlowGenerator>()

    // ===[ PETER JORDAN ]===
    val peterJordanActionFigure = createCortesFlowGenerator<PeterJordanActionFigureCortesFlowGenerator>()

    // ===[ DOUGLAS ]===
    val douglasPointing = createCortesFlowGenerator<DouglasPointingCortesFlowGenerator>()
    val douglasPray = createCortesFlowGenerator<DouglasPrayCortesFlowGenerator>()
    val douglasLaughing = createCortesFlowGenerator<DouglasLaughingCortesFlowGenerator>()

    // ===[ POLADOFUL ]===
    val poladofulDiscussion = createCortesFlowGenerator<PoladofulDiscussionCortesFlowGenerator>()

    // ===[ METAFORANDO ]===
    val metaforandoBadge = createCortesFlowGenerator<MetaforandoBadgeCortesFlowGenerator>()
    val metaforandoSurprised = createCortesFlowGenerator<MetaforandoSurprisedCortesFlowGenerator>()

    // ===[ RATO BORRACHUDO ]===
    val ratoBorrachudoDisappointed = createCortesFlowGenerator<RatoBorrachudoDisappointedCortesFlowGenerator>()
    val ratoBorrachudoNoGlasses = createCortesFlowGenerator<RatoBorrachudoNoGlassesCortesFlowGenerator>()

    // ===[ ARTHUR BENOZATTI ]===
    val arthurBenozzatiSmileCortesFlowGenerator = createCortesFlowGenerator<ArthurBenozzatiSmileCortesFlowGenerator>()

    // ===[ JÚLIO COCIELO ]===
    val julioCocieloEyesCortesFlowGenerator = createCortesFlowGenerator<JulioCocieloEyesCortesFlowGenerator>()

    inline fun <reified T> createCortesFlowGenerator() = T::class.constructors.first().call(
            ImageIO.read(
                    T::class.java.getResourceAsStream("/image_templates/cortes_flow/${convertToSnakeCase(T::class.simpleName!!)}.jpg"),
            ),
            montserratExtraBold
    )
}