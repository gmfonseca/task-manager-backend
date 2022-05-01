package br.com.gmfonseca.taskmanager.domain.usecases

import br.com.gmfonseca.taskmanager.domain.entities.Task
import br.com.gmfonseca.taskmanager.domain.repositories.TaskRepository
import java.util.concurrent.atomic.AtomicInteger

interface CreateTaskUseCase : UseCase<CreateTaskUseCaseParams, Task>
data class CreateTaskUseCaseParams(val task: Task) : Params

class CreateTaskUseCaseImpl(private val taskRepository: TaskRepository) : CreateTaskUseCase {
    override suspend fun execute(params: CreateTaskUseCaseParams): Task =
        taskRepository.createTask(
            params.task.copy(id = "${autoIncrement.getAndIncrement()}")
        )

    private companion object {
        var autoIncrement = AtomicInteger(1)
    }
}
