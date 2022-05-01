package br.com.gmfonseca.taskmanager.contracts.rest

import br.com.gmfonseca.taskmanager.contracts.HttpMethod

@Suppress("UNUSED")
annotation class RoutePath(val name: String, val method: HttpMethod)
