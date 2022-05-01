package br.com.gmfonseca.taskmanager.domain.entities

data class Task(
    val id: String,
    val title: String,
    val description: String,
    var isCompleted: Boolean,
    var imagePath: String?
)
