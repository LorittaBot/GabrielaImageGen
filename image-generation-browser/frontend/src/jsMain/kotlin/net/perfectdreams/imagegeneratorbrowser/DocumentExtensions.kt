package net.perfectdreams.imagegeneratorbrowser

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.events.Event

fun Document.onDOMReady(callback: (Event) -> (Unit)) {
    this.addEventListener("DOMContentLoaded", callback, false)
}

fun Element.onClick(callback: (Event) -> (Unit)) {
    this.addEventListener("click", callback)
}