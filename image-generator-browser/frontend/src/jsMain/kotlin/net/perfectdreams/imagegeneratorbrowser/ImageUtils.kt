package net.perfectdreams.imagegeneratorbrowser

import org.w3c.dom.Image
import org.w3c.dom.events.Event
import org.w3c.files.Blob
import org.w3c.files.FileReader
import org.w3c.files.get
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun Image.awaitLoad(url: String) {
    return kotlin.coroutines.suspendCoroutine { cont ->
        this.onload = {
            cont.resume(Unit)
        }
        this.onerror = { b: dynamic, s: String, i: Int, i1: Int, any: Any? ->
            cont.resumeWithException(Exception())
        }
        this.src = url
    }
}

suspend fun FileReader.awaitLoad(blob: Blob) {
    return kotlin.coroutines.suspendCoroutine { cont ->
        this.onload = {
            cont.resume(Unit)
        }
        this.onerror = { event: Event ->
            cont.resumeWithException(Exception())
        }
        this.readAsDataURL(blob)
    }
}