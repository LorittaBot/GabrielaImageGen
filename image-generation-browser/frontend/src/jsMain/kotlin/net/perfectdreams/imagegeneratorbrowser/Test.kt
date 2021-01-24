package net.perfectdreams.imagegeneratorbrowser

import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.dom.clear
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onChangeFunction
import net.perfectdreams.imagegen.generators.GeneratorInfo
import net.perfectdreams.imagegen.generators.GeneratorsInfo
import net.perfectdreams.imagegen.graphics.*
import net.perfectdreams.imagegen.utils.extensions.get2dContext
import org.w3c.dom.*
import org.w3c.dom.Image
import org.w3c.files.Blob
import org.w3c.files.FileReader
import org.w3c.files.get

fun main() {
    println("Hello!")

    document.onDOMReady {
        val generatorWrapper = document.querySelector("#generator-wrapper") as HTMLDivElement

        val allGenerators = GeneratorsInfo.imageGenerators

        val url = document.location?.pathname

        println("URL: $url")

        val activeGenerator = allGenerators.firstOrNull {
            println("Generator URL: ${"/${it.urlPath}"}")
            url == "/${it.urlPath}"
        }

        println("Active Generator: $activeGenerator")

        if (activeGenerator != null) {
            generatorWrapper.append.div {
                p {
                    +"Imagem"
                }

                input(type = InputType.file) {
                    accept = "image/*"
                    id = "myfile"

                    onChangeFunction = {
                        println("Changed")

                        val selecter = document.querySelector("#myfile")
                                as HTMLInputElement

                        GlobalScope.launch {
                            val reader = FileReader()
                            reader.awaitLoad(selecter.files!![0] as Blob)
                            println("Loaded")

                            val sourceImage = Image()
                            sourceImage.awaitLoad(reader.result)
                            sourceImage.src = reader.result

                            renderTemplate(activeGenerator, sourceImage)
                        }
                    }
                }

                div {
                    id = "generator-result"
                }
            }

            GlobalScope.launch {
                val defaultImageSource = Image()
                defaultImageSource.awaitLoad("/assets/img/default_source.png")

                renderTemplate(activeGenerator, defaultImageSource)
            }
        }
    }
}

fun renderTemplate(generatorInfo: GeneratorInfo, sourceImage: Image) {
    val generatorResult = (document.querySelector("#generator-result") as HTMLDivElement)

    val selecter = document.querySelector("#myfile")
            as HTMLInputElement

    GlobalScope.launch {
        val filePath = "assets/img/templates/${generatorInfo.path}/template.png"

        val image = Image()
        image.awaitLoad(filePath)

        val canvas = createCanvas(image.width, image.height)
        canvas.get2dContext().drawImage(image, 0.0, 0.0)
        val template = JSImage(canvas)

        val generator = generatorInfo.generator.invoke(template)

        val sourceCanvas = createCanvas(sourceImage.width, sourceImage.height)
        sourceCanvas.get2dContext().drawImage(
                sourceImage,
                0.0,
                0.0
        )

        val jsImage = JSImage(sourceCanvas)

        val result = generator.generate(jsImage)

        result as JSImage
        val stuff = result.canvas.toDataURL("image/png")

        val imageResult = Image()
        imageResult.src = stuff

        generatorResult.clear()

        generatorResult.appendChild(imageResult)

        generatorResult.append {
            div {
                a(href = stuff) {
                    attributes["download"] = "${generatorInfo.urlPath}.png"

                    +"Download"
                }
            }
        }
    }
}