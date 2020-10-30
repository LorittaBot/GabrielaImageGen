package net.perfectdreams.imageserver

fun main() {
    val a = GabrielaImageGen::class.java.classLoader.getResourceAsStream("ednaldo_bandeira/template.png")

    println(a)
}