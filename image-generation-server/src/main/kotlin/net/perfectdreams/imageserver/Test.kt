package net.perfectdreams.imageserver

fun main() {
    val a = GabrielaImageGen::class.java.classLoader.getResourceAsStream("image_templates/ednaldo_bandeira/template.png")

    println(a)
}