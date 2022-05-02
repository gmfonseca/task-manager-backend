package br.com.gmfonseca.taskmanager.domain.usecases

import br.com.gmfonseca.taskmanager.domain.entities.Task
import br.com.gmfonseca.taskmanager.domain.repositories.TaskRepository
import br.com.gmfonseca.taskmanager.utils.models.FileData
import java.io.File

/**
 * The use case to complete a task and save its image,
 * all provided through [CompleteTaskUseCaseParams] params
 */
interface CompleteTaskUseCase : UseCase<CompleteTaskUseCaseParams, Task?>

/**
 * The definition of the [CompleteTaskUseCase] use case parameters.
 *
 * @param taskId The id of the desired task to be completed
 * @param fileData The image data of the completed task
 */
data class CompleteTaskUseCaseParams(val taskId: String, val fileData: FileData) : Params

class CompleteTaskUseCaseImpl(private val taskRepository: TaskRepository) : CompleteTaskUseCase {

    private val uploadsDirectory: File by lazy {
        File("uploads").apply { if (!exists()) mkdirs() }
    }

    override suspend fun execute(params: CompleteTaskUseCaseParams): Task? {
        return taskRepository.findTaskById(params.taskId)?.let { task ->
            val imageFile = createImageFile(params.fileData)

            task.markAsComplete(taskImagePath = imageFile.absolutePath)

            taskRepository.updateTask(task)
        }
    }

    private fun createImageFile(fileData: FileData): File {
        return File(uploadsDirectory, fileData.name).apply {
            if (exists()) delete()

            writeBytes(fileData.bytes)
        }
    }

    private fun Task.markAsComplete(taskImagePath: String) {
        if (isCompleted) {
            deleteCurrentImage()
        } else {
            isCompleted = true
        }

        imagePath = taskImagePath
    }

    private fun Task.deleteCurrentImage() {
        imagePath?.let { File(it).delete() }
    }
}
