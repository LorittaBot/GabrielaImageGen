package net.perfectdreams.imageserver

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.*
import mu.KotlinLogging
import net.perfectdreams.imagegen.generators.GeneratorsInfo
import net.perfectdreams.imageserver.routes.BaseRoute

class GabrielaImageGen {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    val routes = mutableListOf<BaseRoute>()

    fun start() {
        val allGenerators = GeneratorsInfo.imageGenerators

        val server = embeddedServer(Netty, port = 8001) {
            routing {
                get("/") {
                    call.respondHtml {
                        head {
                            title("Gabriela Image Generation")
                            styleLink("/assets/css/style.css")
                            script(src = "/assets/js/frontend.js") {}
                        }

                        body {
                            nav(classes = "navigation-bar fixed") {
                                div(classes = "left-side-entries") {
                                    div(classes = "entry") {
                                        + "OwO"
                                    }
                                }
                            }
                            div(classes = "dummy-navigation-bar") {}

                            div(classes = "centered-text") {
                                id = "one-column"

                                div(classes = "media") {
                                    div(classes = "media-body") {
                                        p {
                                            repeat(25) {
                                                +"owo whats this? "
                                            }
                                        }
                                    }

                                    div(classes = "media-figure") {
                                        img(src = "/assets/img/gabriela_draw.png") {
                                            style = "width: auto;"
                                        }
                                    }
                                }

                                for (generator in allGenerators) {
                                    div {
                                        a(href = "/${generator.urlPath}") {
                                            +generator.name
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                for (generator in allGenerators) {
                    get("/${generator.path.replace("_", "-")}/") {
                        call.respondHtml {
                            head {
                                title("Generator ${generator.name}")
                                styleLink("/assets/css/style.css")
                                script(src = "/assets/js/frontend.js") {}
                            }

                            body {
                                nav(classes = "navigation-bar fixed") {
                                    div(classes = "left-side-entries") {
                                        div(classes = "entry") {
                                            + "OwO"
                                        }
                                    }
                                }
                                div(classes = "dummy-navigation-bar") {}

                                div(classes = "centered-text") {
                                    id = "one-column"

                                    h1 {
                                        + generator.name
                                    }

                                    div {
                                        id = "generator-wrapper"
                                    }
                                }
                            }
                        }
                    }
                }

                static("/assets/") {
                    resources("/static/assets/")
                }

                static("/assets/img/templates/") {
                    resources("/image_templates/")
                }

                for (route in routes)
                    route.register(this)
            }
        }
        server.start(wait = true)
    }
}