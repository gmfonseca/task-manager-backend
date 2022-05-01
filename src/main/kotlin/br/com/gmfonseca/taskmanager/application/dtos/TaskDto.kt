package br.com.gmfonseca.taskmanager.application.dtos

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class TaskDto(
    val id: String = "",
    val title: String,
    val description: String,
    @SerialName("is_completed") var isCompleted: Boolean = false,
    @SerialName("image_path") var imagePath: String? = null
)
