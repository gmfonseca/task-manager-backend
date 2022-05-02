package br.com.gmfonseca.taskmanager.utils.mappers

import br.com.gmfonseca.taskmanager.application.dtos.TaskDto
import br.com.gmfonseca.taskmanager.data.entities.Task as EntityTask
import br.com.gmfonseca.taskmanager.domain.entities.Task as DomainTask

/**
 * Construct a [TaskDto] instance based on the given [DomainTask]
 */
val DomainTask.asDto
    get() = TaskDto(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        imagePath = imagePath,
    )

/**
 * Construct an [EntityTask] instance based on the given [DomainTask]
 */
val DomainTask.asEntity
    get() = EntityTask(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        imagePath = imagePath,
    )

/**
 * Construct a [DomainTask] instance based on the given [EntityTask]
 */
val EntityTask.asDomain
    get() = DomainTask(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        imagePath = imagePath,
    )

/**
 * Construct a [DomainTask] instance based on the given [TaskDto]
 */
val TaskDto.asDomain
    get() = DomainTask(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        imagePath = imagePath,
    )
