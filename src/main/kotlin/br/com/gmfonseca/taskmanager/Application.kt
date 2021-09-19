package br.com.gmfonseca.taskmanager

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.serialization.json
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {

    // Negotiating media types between the client and server. For this, it uses the Accept and Content-Type headers.
    install(ContentNegotiation) {
        json() // adding support to json
    }
}