package net.perfectdreams.gabrielaimageserver.webserver.generators

import net.perfectdreams.gabrielaimageserver.generators.cortesflow.ArthurBenozzatiSmileCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.DouglasLaughingCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.DouglasPointingCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.DouglasPrayCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.GaulesSadCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.IgorAngryCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.IgorNakedCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.IgorPointingCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.JulioCocieloEyesCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.LucasInutilismoExaltedCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.MetaforandoBadgeCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.MetaforandoSurprisedCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.MiticoSuccCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.MonarkDiscussionCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.MonarkSmokingCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.MonarkStopCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.PeterJordanActionFigureCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.PoladofulDiscussionCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.RatoBorrachudoDisappointedCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.cortesflow.RatoBorrachudoNoGlassesCortesFlowGenerator
import net.perfectdreams.gabrielaimageserver.generators.utils.GeneratorsUtils
import net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGen
import java.awt.Font
import java.io.File
import javax.imageio.ImageIO

class CortesFlowGenerators(val m: GabrielaImageGen) {
    val montserratExtraBold = Font.createFont(
        Font.TRUETYPE_FONT,
        File(m.config.assetsFolder + "/fonts/montserrat-extrabold.otf")
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
            File("${m.config.assetsFolder}/image_templates/cortes_flow/${GeneratorsUtils.convertToSnakeCase(T::class.simpleName!!)}.jpg"),
        ),
        montserratExtraBold
    )
}