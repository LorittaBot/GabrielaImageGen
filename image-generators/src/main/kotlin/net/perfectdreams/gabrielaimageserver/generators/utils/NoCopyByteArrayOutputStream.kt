package net.perfectdreams.gabrielaimageserver.generators.utils

import java.io.ByteArrayOutputStream

/**
 * A [ByteArrayOutputStream] that doesn't create a [ByteArrayOutputStream.buf] copy when calling [toByteArray]
 *
 * @see https://stackoverflow.com/a/12253091/7271796
 */
class NoCopyByteArrayOutputStream : ByteArrayOutputStream() {
    @Synchronized
    override fun toByteArray(): ByteArray {
        return this.buf
    }
}