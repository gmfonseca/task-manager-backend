package br.com.gmfonseca.taskmanager

import br.com.gmfonseca.taskmanager.application.routes.TasksRouting
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {

    // Negotiating media types between the client and server. For this, it uses the Accept and Content-Type headers.
    install(ContentNegotiation) {
        json() // adding support to json
    }

    install(CORS) {
        anyHost()
    }

    TasksRouting(application = this)
}
