package net.perfectdreams.imagegen.graphics

@JsName("Buffer")
external class Buffer {
    companion object {
        fun from(data: String, encoding: String): Buffer
        fun alloc(size: Int): Buffer
        fun allocUnsafe(size: Int): Buffer
    }

    val length: Int
    fun values(): dynamic
    fun writeInt8(value: Int): Int
    fun writeInt8(value: Int, offset: Int): Int
}

fun Buffer.toByteArray(): ByteArray {
    val imageByteArray = ByteArray(this.length)

    console.log(this.values())
    var idx = 0
    val values = this.values()

    while (true) {
        val result = values.next()
        if (result.done == true)
            break

        imageByteArray[idx] = result.value
        idx++
    }

    return imageByteArray
}

fun ByteArray.toBuffer(): Buffer {
    val buffer = Buffer.alloc(this.size)

    var currentOffset = 0
    for (byte in this)
        currentOffset = buffer.writeInt8(byte.toInt(), currentOffset)

    return buffer
}