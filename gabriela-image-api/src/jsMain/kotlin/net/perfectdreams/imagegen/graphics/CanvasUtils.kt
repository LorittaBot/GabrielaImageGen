package net.perfectdreams.imagegen.graphics

import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement

fun createCanvas(width: Int, height: Int): HTMLCanvasElement {
    return document.createElement("CANVAS")
        .apply {
            setAttribute("width", width.toString())
            setAttribute("height", height.toString())
        } as HTMLCanvasElement
}