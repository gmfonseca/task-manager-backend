package br.com.gmfonseca.taskmanager.domain.usecases

import br.com.gmfonseca.taskmanager.domain.entities.Task
import br.com.gmfonseca.taskmanager.domain.repositories.TaskRepository

/**
 * The use case to find a specific task, which is
 * chosen through [FindTaskUseCaseByIdParams] params.
 */
interface FindTaskUseCaseById : UseCase<FindTaskUseCaseByIdParams, Task?>

/**
 * The definition of the [FindTaskUseCaseById] use case parameters.
 *
 * @param taskId The id of the desired task to find
 */
data class FindTaskUseCaseByIdParams(val taskId: String) : Params

class FindTaskUseCaseByIdImpl(private val taskRepository: TaskRepository) : FindTaskUseCaseById {
    override suspend fun execute(params: FindTaskUseCaseByIdParams): Task? =
        taskRepository.findTaskById(params.taskId)
}
