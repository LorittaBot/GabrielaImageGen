package net.perfectdreams.gabrielaimageserver.exceptions

class UntrustedURLException(val url: String) : IllegalArgumentException("$url is not trusted!")