package br.com.gmfonseca.taskmanager.domain.usecases

import br.com.gmfonseca.taskmanager.domain.entities.Task
import br.com.gmfonseca.taskmanager.domain.repositories.TaskRepository

interface FindTaskUseCaseById : UseCase<FindTaskUseCaseByIdParams, Task?>
data class FindTaskUseCaseByIdParams(val taskId: String) : Params

class FindTaskUseCaseByIdImpl(private val taskRepository: TaskRepository) : FindTaskUseCaseById {
    override suspend fun execute(params: FindTaskUseCaseByIdParams): Task? = taskRepository.findTaskById(params.taskId)
}
