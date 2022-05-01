package br.com.gmfonseca.taskmanager.domain.usecases

import br.com.gmfonseca.taskmanager.domain.entities.Task
import br.com.gmfonseca.taskmanager.domain.repositories.TaskRepository
import java.io.File

interface CompleteTaskUseCase : UseCase<CompleteTaskUseCaseParams, Task?>
data class CompleteTaskUseCaseParams(val taskId: String, val fileName: String, val fileBytes: ByteArray) : Params

class CompleteTaskUseCaseImpl(private val taskRepository: TaskRepository) : CompleteTaskUseCase {

    private val uploadsDir: File by lazy {
        File("uploads").apply { if (!exists()) mkdirs() }
    }

    override suspend fun execute(params: CompleteTaskUseCaseParams): Task? {
        return taskRepository.findTaskById(params.taskId)?.let { task ->
            val file = File(uploadsDir, params.fileName).apply {
                if (exists()) delete()

                writeBytes(params.fileBytes)
            }

            task.updateTaskImage(filePath = file.absolutePath)

            taskRepository.updateTask(task)
        }
    }

    private fun Task.updateTaskImage(filePath: String) {
        if (!isCompleted) {
            isCompleted = true
        } else {
            imagePath?.let { File(it).delete() }
        }

        imagePath = filePath
    }
}
