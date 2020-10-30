package net.perfectdreams.imagegen.graphics

class JVMGraphics(val handle: java.awt.Graphics) : Graphics {
	override fun drawImage(image: Image, x: Int, y: Int) {
		image as JVMImage
		handle.drawImage(image.handle, x, y, null)
	}
}