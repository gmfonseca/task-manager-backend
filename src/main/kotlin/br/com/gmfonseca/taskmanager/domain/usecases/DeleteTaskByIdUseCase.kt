package br.com.gmfonseca.taskmanager.domain.usecases

import br.com.gmfonseca.taskmanager.domain.repositories.TaskRepository

/**
 * The use case to delete a specific task, which is
 * chosen through [DeleteTaskByIdUseCaseParams] params.
 */
interface DeleteTaskByIdUseCase : UseCase<DeleteTaskByIdUseCaseParams, Boolean>

/**
 * The definition of the [DeleteTaskByIdUseCase] use case parameters.
 *
 * @param taskId The id of the desired task to be deleted
 */
data class DeleteTaskByIdUseCaseParams(val taskId: String) : Params

class DeleteTaskByIdUseCaseImpl(private val taskRepository: TaskRepository) : DeleteTaskByIdUseCase {
    override suspend fun execute(params: DeleteTaskByIdUseCaseParams): Boolean =
        taskRepository.deleteTaskById(taskId = params.taskId)
}
