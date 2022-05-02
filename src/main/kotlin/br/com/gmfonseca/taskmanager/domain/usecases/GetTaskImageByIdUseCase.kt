package br.com.gmfonseca.taskmanager.domain.usecases

import br.com.gmfonseca.taskmanager.domain.entities.Task
import br.com.gmfonseca.taskmanager.domain.repositories.TaskRepository
import io.ktor.features.*
import java.io.File

/**
 * The use case to get the image of a specific task, which is
 * chosen through [GetTaskImageByIdUseCaseParams] params.
 */
interface GetTaskImageByIdUseCase : UseCase<GetTaskImageByIdUseCaseParams, File>

/**
 * The definition of the [GetTaskImageByIdUseCase] use case parameters.
 *
 * @param taskId The id of the desired task to get the image
 */
data class GetTaskImageByIdUseCaseParams(val taskId: String) : Params

class GetTaskImageByIdUseCaseImpl(private val taskRepository: TaskRepository) : GetTaskImageByIdUseCase {
    override suspend fun execute(params: GetTaskImageByIdUseCaseParams): File {
        val task = getTaskById(params.taskId)
        val imagePath = getTaskImagePath(task)

        return getImageFile(imagePath, task)
    }

    private fun getImageFile(imagePath: String, task: Task): File {
        val imageFile = File(imagePath)

        return if (imageFile.exists()) imageFile
        else throw NotFoundException("No image found for task ${task.id}!")
    }

    private fun getTaskImagePath(task: Task): String {
        return task.imagePath?.takeIf(String::isNotBlank)
            ?: throw NotFoundException("The task ${task.id} has no image.")
    }

    private suspend fun getTaskById(taskId: String): Task {
        return taskRepository.findTaskById(taskId = taskId)
            ?: throw NotFoundException("Task not found!")
    }
}
