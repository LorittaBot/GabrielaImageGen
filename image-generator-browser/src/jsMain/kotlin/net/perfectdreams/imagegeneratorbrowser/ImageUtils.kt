package net.perfectdreams.imagegeneratorbrowser

import org.w3c.dom.Image
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