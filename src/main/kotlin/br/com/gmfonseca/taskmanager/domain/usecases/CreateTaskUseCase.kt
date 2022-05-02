package br.com.gmfonseca.taskmanager.domain.usecases

import br.com.gmfonseca.taskmanager.domain.entities.Task
import br.com.gmfonseca.taskmanager.domain.repositories.TaskRepository
import java.util.concurrent.atomic.AtomicInteger

/**
 * The use case to create a new task based on the given template,
 * provided through [CompleteTaskUseCaseParams] params, and
 * ignore the id of the template, generating a new one.
 */
interface CreateTaskUseCase : UseCase<CreateTaskUseCaseParams, Task>

/**
 * The definition of the [CreateTaskUseCase] use case parameters.
 *
 * @param task The template of the task to be created
 */
data class CreateTaskUseCaseParams(val task: Task) : Params

class CreateTaskUseCaseImpl(private val taskRepository: TaskRepository) : CreateTaskUseCase {
    override suspend fun execute(params: CreateTaskUseCaseParams): Task {
        val taskDefinition = createTaskDefinitionBasedOn(params.task)

        return taskRepository.createTask(taskDefinition)
    }

    private fun createTaskDefinitionBasedOn(task: Task) =
        task.copy(id = "${autoIncrement.getAndIncrement()}")

    private companion object {
        var autoIncrement = AtomicInteger(1)
    }
}
