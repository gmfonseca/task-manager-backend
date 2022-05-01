package br.com.gmfonseca.taskmanager.domain.usecases

import br.com.gmfonseca.taskmanager.domain.repositories.TaskRepository
import io.ktor.features.*
import java.io.File

interface GetTaskImageByIdUseCase : UseCase<GetTaskImageByIdUseCaseParams, File?>
data class GetTaskImageByIdUseCaseParams(val taskId: String) : Params

class GetTaskImageByIdUseCaseImpl(private val taskRepository: TaskRepository) : GetTaskImageByIdUseCase {
    override suspend fun execute(params: GetTaskImageByIdUseCaseParams): File? {
        val task = taskRepository.findTaskById(taskId = params.taskId)
            ?: throw NotFoundException("Task not found!")

        val imagePath = task.imagePath?.takeIf(String::isNotBlank)
            ?: throw NotFoundException("The task ${task.id} has no image.")

        return File(imagePath).takeIf(File::exists)
    }
}
