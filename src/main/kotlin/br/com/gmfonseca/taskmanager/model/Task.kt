package br.com.gmfonseca.taskmanager.model

import kotlinx.serialization.Serializable

val taskStorage = mutableListOf<Task>()
private var autoIncrement = 1

@Serializable
data class Task(
    val id: String = "${autoIncrement++}",
    val title: String,
    val description: String,
    var isCompleted: Boolean = false,
    var imagePath: String? = null
)
