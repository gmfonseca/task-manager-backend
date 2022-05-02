package br.com.gmfonseca.taskmanager.domain.usecases

import br.com.gmfonseca.taskmanager.domain.entities.Task
import br.com.gmfonseca.taskmanager.domain.repositories.TaskRepository

/**
 * The use case to list tasks filtered by completion status or
 * list all if the desired status was not provided, which is
 * chosen through [ListTasksUseCaseParams] params.
 */
interface ListTasksUseCase : UseCase<ListTasksUseCaseParams, List<Task>>

/**
 * The definition of the [GetTaskImageByIdUseCase] use case parameters.
 *
 * @param isCompleted The desired value to filter tasks, where `null` means no filter
 */
data class ListTasksUseCaseParams(val isCompleted: Boolean?) : Params

class ListTasksUseCaseImpl(private val taskRepository: TaskRepository) : ListTasksUseCase {
    override suspend fun execute(params: ListTasksUseCaseParams): List<Task> =
        if (params.isCompleted != null) {
            taskRepository.listTasksByCompletionStatus(params.isCompleted)
        } else {
            taskRepository.listTasks()
        }
}
