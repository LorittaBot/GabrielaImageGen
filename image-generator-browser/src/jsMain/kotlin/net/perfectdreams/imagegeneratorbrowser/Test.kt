package net.perfectdreams.imagegeneratorbrowser

import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import net.perfectdreams.imagegen.generators.skewed.CanellaDVDGenerator
import net.perfectdreams.imagegen.generators.skewed.EdnaldoBandeiraGenerator
import net.perfectdreams.imagegen.generators.skewed.EdnaldoTVGenerator
import net.perfectdreams.imagegen.generators.skewed.MonicaAtaGenerator
import net.perfectdreams.imagegen.graphics.*
import net.perfectdreams.imagegen.utils.extensions.get2dContext
import org.w3c.dom.*
import org.w3c.dom.Image
import org.w3c.dom.events.Event
import org.w3c.files.Blob
import org.w3c.files.FileReader
import org.w3c.files.get

fun main() {
    println("Hello!")

    val body = document.body!!

    val baseStuff = body.append.div {
        select {
            id = "select-meme"
            option {
                value = "ednaldo-bandeira"
                + "Ednaldo Bandeira"
            }

            option {
                value = "ednaldo-tv"
                + "Ednaldo TV"
            }

            option {
                value = "canella-dvd"
                + "Canella DVD"
            }

            option {
                value = "monica-ata"
                + "Mônica Ata"
            }

            onChangeFunction = {
                console.log(it.target)
                console.log((it.target as HTMLSelectElement).value)
               //  it.
            }
        }

        hr {}

        p {
          + "Imagem"
        }

        input(type = InputType.file) {
            id = "myfile"
        }

        input(type = InputType.submit) {
            id = "submit"
            value = "Criar Imagem"

            onClickFunction = {
                val selectMeme = document.querySelector("#select-meme") as HTMLSelectElement

                val selectedMeme = selectMeme.value

                val selecter = document.querySelector("#myfile")
                        as HTMLInputElement

                GlobalScope.launch {
                    val filePath = when (selectedMeme) {
                        "ednaldo-tv" -> "assets/ednaldo_tv/template.png"
                        "ednaldo-bandeira" -> "assets/ednaldo_bandeira/template.png"
                        "canella-dvd" -> "assets/canella_dvd/template.png"
                        "monica-ata" -> "assets/monica_ata/template.png"
                        else -> throw IllegalArgumentException("Invalid Meme! $selectedMeme")
                    }

                    val image = Image()
                    image.awaitLoad(filePath)

                    val canvas = createCanvas(image.width, image.height)
                    canvas.get2dContext().drawImage(image, 0.0, 0.0)
                    val template = JSImage(canvas)

                    val generator = when (selectedMeme) {
                        "ednaldo-tv" -> EdnaldoTVGenerator(template)
                        "ednaldo-bandeira" -> EdnaldoBandeiraGenerator(template)
                        "canella-dvd" -> CanellaDVDGenerator(template)
                        "monica-ata" -> MonicaAtaGenerator(template)
                        else -> throw IllegalArgumentException("Invalid Generator! $selectedMeme")
                    }

                    val reader = FileReader()

                    reader.onload = {
                        println("Loaded")

                        val image = Image()
                        image.src = reader.result

                        image.onload = {
                            val sourceCanvas = createCanvas(image.width, image.height)
                            sourceCanvas.get2dContext().drawImage(
                                image,
                                0.0,
                                0.0
                            )

                            val jsImage = JSImage(sourceCanvas)

                            val result = generator.generate(jsImage)

                            body.append {
                                div {
                                    + "Canvas"
                                }
                            }

                            body.appendChild((result as JSImage).canvas)

                            body.append {
                                div {
                                    + "As Image"
                                }
                            }

                            result as JSImage
                            val stuff = result.canvas.toDataURL("image/png")

                            val image = Image()
                            image.src = stuff
                            body.appendChild(image)

                            "".asDynamic()
                        }

                        "a".asDynamic()
                    }
                    reader.readAsDataURL(selecter.files!![0] as Blob)
                }
            }
        }
    }

    /* val selecter = document.querySelector("#myfile")
            as HTMLInputElement

    val test = document.querySelector("#my-canvas") as HTMLCanvasElement
    val ednaldoBandeiraGenerator = EdnaldoTVGenerator(JSImage(test))

    (body.querySelector("#submitimage") as HTMLInputElement)
        .onClick {
            println("clicked")

            val reader = FileReader()

            reader.onload = {
                println("Loaded")

                val image = Image()
                image.src = reader.result

                image.onload = {
                    val sourceCanvas = createCanvas(image.width, image.height)
                    sourceCanvas.get2dContext().drawImage(
                        image,
                        0.0,
                        0.0
                    )

                    val jsImage = JSImage(sourceCanvas)

                    val result = ednaldoBandeiraGenerator.generate(jsImage)

                    body.appendChild((result as JSImage).canvas)

                    result as JSImage
                    val stuff = result.canvas.toDataURL("image/png")

                    val image = Image()
                    image.src = stuff
                    body.appendChild(image)

                    "".asDynamic()
                }

                "a".asDynamic()
            }
            reader.readAsDataURL(selecter.files!![0] as Blob)
        } */
}

fun Element.onClick(callback: (Event) -> (Unit)) {
    this.addEventListener("click", callback)
}