package br.com.gmfonseca.taskmanager.utils.mappers

import br.com.gmfonseca.taskmanager.application.dtos.TaskDto
import br.com.gmfonseca.taskmanager.data.entities.Task as EntityTask
import br.com.gmfonseca.taskmanager.domain.entities.Task as DomainTask

val DomainTask.asDto get() = TaskDto(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    imagePath = imagePath,
)

val DomainTask.asEntity get() = EntityTask(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    imagePath = imagePath,
)

val EntityTask.asDomain get() = DomainTask(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    imagePath = imagePath,
)

val TaskDto.asDomain get() = DomainTask(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    imagePath = imagePath,
)
