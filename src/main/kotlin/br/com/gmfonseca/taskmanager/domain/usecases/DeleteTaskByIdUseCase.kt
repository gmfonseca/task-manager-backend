package br.com.gmfonseca.taskmanager.domain.usecases

import br.com.gmfonseca.taskmanager.domain.repositories.TaskRepository

interface DeleteTaskByIdUseCase : UseCase<DeleteTaskByIdUseCaseParams, Boolean>
data class DeleteTaskByIdUseCaseParams(val taskId: String) : Params

class DeleteTaskByIdUseCaseImpl(private val taskRepository: TaskRepository) : DeleteTaskByIdUseCase {
    override suspend fun execute(params: DeleteTaskByIdUseCaseParams): Boolean =
        taskRepository.deleteTaskById(taskId = params.taskId)
}
