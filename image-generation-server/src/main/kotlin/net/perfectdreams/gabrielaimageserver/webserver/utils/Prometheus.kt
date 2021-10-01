package net.perfectdreams.gabrielaimageserver.webserver.utils

import io.prometheus.client.Histogram

/**
 * Used to hold Prometheus instrumentations
 */
object Prometheus {
    val GENERATOR_LATENCY_COUNT = Histogram.build()
        .name("generator_latency")
        .help("Generator Latency")
        .labelNames("generator")
        .buckets(.005, .01, .025, .05, .075, .1, .25, .5, .75, 1.0, 2.5, 5.0, 7.5, 10.0, 12.5, 15.0, 17.5, 20.0)
        .create()

    fun register() {
        GENERATOR_LATENCY_COUNT.register<Histogram>()
    }
}