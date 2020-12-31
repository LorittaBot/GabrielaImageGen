package net.perfectdreams.imagegen.generators

import net.perfectdreams.imagegen.generators.skewed.*

object GeneratorsInfo {
    val imageGenerators = listOf(
            GeneratorInfo(
                    "Art",
                    "art"
            ) {
                ArtGenerator(it)
            },

            GeneratorInfo(
                    "Bob Burning Paper",
                    "bob_burning_paper"
            ) {
                BobBurningPaperGenerator(it)
            },

            GeneratorInfo(
                    "Bolsonaro Frame",
                    "bolso_frame"
            ) {
                BolsoFrameGenerator(it)
            },

            GeneratorInfo(
                    "Bolsonaro",
                    "bolsonaro"
            ) {
                BolsonaroGenerator(it)
            },

            GeneratorInfo(
                    "Bolsonaro 2",
                    "bolsonaro2"
            ) {
                Bolsonaro2Generator(it)
            },

            GeneratorInfo(
                    "Briggs Cover",
                    "briggs_cover"
            ) {
                BriggsCoverGenerator(it)
            },

            GeneratorInfo(
                    "Buck Shirt",
                    "buck_shirt"
            ) {
                BuckShirtGenerator(it)
            },

            GeneratorInfo(
                    "Canella DVD",
                    "canella_dvd"
            ) {
                CanellaDVDGenerator(it)
            },

            GeneratorInfo(
                    "Chico Ata",
                    "chico_ata"
            ) {
                ChicoAtaGenerator(it)
            },

            GeneratorInfo(
                    "Ednaldo Bandeira",
                    "ednaldo_bandeira"
            ) {
                EdnaldoBandeiraGenerator(it)
            },

            GeneratorInfo(
                    "Ednaldo TV",
                    "ednaldo_tv"
            ) {
                EdnaldoTVGenerator(it)
            },

            GeneratorInfo(
                    "Gessy Ata",
                    "gessy_ata"
            ) {
                GessyAtaGenerator(it)
            },

            GeneratorInfo(
                    "Lori Ata",
                    "lori_ata"
            ) {
                LoriAtaGenerator(it)
            },

            GeneratorInfo(
                    "Lori Sign",
                    "lori_sign"
            ) {
                LoriSignGenerator(it)
            },

            GeneratorInfo(
                    "Mônica Ata",
                    "monica_ata"
            ) {
                MonicaAtaGenerator(it)
            },

            GeneratorInfo(
                    "Passing Paper",
                    "passing_paper"
            ) {
                PassingPaperGenerator(it)
            },

            GeneratorInfo(
                    "Romero Britto",
                    "romero_britto"
            ) {
                RomeroBrittoGenerator(it)
            },

            GeneratorInfo(
                    "Wolverine Frame",
                    "wolverine_frame"
            ) {
                WolverineFrameGenerator(it)
            },
    )
}