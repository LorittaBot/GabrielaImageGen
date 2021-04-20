package net.perfectdreams.imageserver.utils

class UntrustedURLException(val url: String) : IllegalArgumentException("$url is not trusted!")