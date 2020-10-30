package net.perfectdreams.imagegen.utils.extensions

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement

fun HTMLCanvasElement.get2dContext() = this.getContext("2d") as CanvasRenderingContext2D