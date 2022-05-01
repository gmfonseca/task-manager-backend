package br.com.gmfonseca.taskmanager.domain.usecases

import br.com.gmfonseca.taskmanager.domain.entities.Task
import br.com.gmfonseca.taskmanager.domain.repositories.TaskRepository

interface ListTasksUseCase : UseCase<ListTasksUseCaseParams, List<Task>>
data class ListTasksUseCaseParams(val isCompleted: Boolean?) : Params

class ListTasksUseCaseImpl(private val taskRepository: TaskRepository) : ListTasksUseCase {
    override suspend fun execute(params: ListTasksUseCaseParams): List<Task> =
        if (params.isCompleted != null) {
            taskRepository.listTasksByCompletionStatus(params.isCompleted)
        } else {
            taskRepository.listTasks()
        }
}
