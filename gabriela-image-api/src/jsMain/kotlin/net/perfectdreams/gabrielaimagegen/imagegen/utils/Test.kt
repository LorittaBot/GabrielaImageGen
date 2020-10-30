package net.perfectdreams.gabrielaimagegen.imagegen.utils

import kotlinx.browser.document
import net.perfectdreams.imagegen.graphics.*
import net.perfectdreams.imagegen.utils.extensions.get2dContext
import org.w3c.dom.Element
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.Image
import org.w3c.dom.events.Event
import org.w3c.files.Blob
import org.w3c.files.FileReader
import org.w3c.files.get

fun main() {
    println("Hello!")

    /* val body = document.body!!
    val selecter = document.querySelector("#myfile")
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