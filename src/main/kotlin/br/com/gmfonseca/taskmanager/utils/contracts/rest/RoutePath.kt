package br.com.gmfonseca.taskmanager.utils.contracts.rest

import br.com.gmfonseca.taskmanager.utils.contracts.HttpMethod
import kotlin.annotation.AnnotationTarget.FUNCTION

@Suppress("UNUSED")
@Target(FUNCTION)
annotation class RoutePath(val name: String, val method: HttpMethod)
