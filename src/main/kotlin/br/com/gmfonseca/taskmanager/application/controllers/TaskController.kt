package br.com.gmfonseca.taskmanager.application.controllers

import br.com.gmfonseca.taskmanager.application.dtos.TaskDto
import br.com.gmfonseca.taskmanager.data.repositories.TaskRepositoryImpl
import br.com.gmfonseca.taskmanager.domain.entities.Task
import br.com.gmfonseca.taskmanager.domain.repositories.TaskRepository
import br.com.gmfonseca.taskmanager.domain.usecases.CompleteTaskUseCase
import br.com.gmfonseca.taskmanager.domain.usecases.CompleteTaskUseCaseImpl
import br.com.gmfonseca.taskmanager.domain.usecases.CompleteTaskUseCaseParams
import br.com.gmfonseca.taskmanager.domain.usecases.CreateTaskUseCase
import br.com.gmfonseca.taskmanager.domain.usecases.CreateTaskUseCaseImpl
import br.com.gmfonseca.taskmanager.domain.usecases.CreateTaskUseCaseParams
import br.com.gmfonseca.taskmanager.domain.usecases.DeleteTaskByIdUseCase
import br.com.gmfonseca.taskmanager.domain.usecases.DeleteTaskByIdUseCaseImpl
import br.com.gmfonseca.taskmanager.domain.usecases.DeleteTaskByIdUseCaseParams
import br.com.gmfonseca.taskmanager.domain.usecases.FindTaskUseCaseById
import br.com.gmfonseca.taskmanager.domain.usecases.FindTaskUseCaseByIdImpl
import br.com.gmfonseca.taskmanager.domain.usecases.FindTaskUseCaseByIdParams
import br.com.gmfonseca.taskmanager.domain.usecases.GetTaskImageByIdUseCase
import br.com.gmfonseca.taskmanager.domain.usecases.GetTaskImageByIdUseCaseImpl
import br.com.gmfonseca.taskmanager.domain.usecases.GetTaskImageByIdUseCaseParams
import br.com.gmfonseca.taskmanager.domain.usecases.ListTasksUseCase
import br.com.gmfonseca.taskmanager.domain.usecases.ListTasksUseCaseImpl
import br.com.gmfonseca.taskmanager.domain.usecases.ListTasksUseCaseParams
import br.com.gmfonseca.taskmanager.utils.mappers.asDomain
import br.com.gmfonseca.taskmanager.utils.mappers.asDto
import br.com.gmfonseca.taskmanager.utils.models.FileData
import java.io.File

class TaskController(
    taskRepository: TaskRepository = TaskRepositoryImpl(),
    private val createTaskUseCase: CreateTaskUseCase = CreateTaskUseCaseImpl(taskRepository),
    private val listTasksUseCase: ListTasksUseCase = ListTasksUseCaseImpl(taskRepository),
    private val findTaskUseCaseById: FindTaskUseCaseById = FindTaskUseCaseByIdImpl(taskRepository),
    private val completeTaskUseCase: CompleteTaskUseCase = CompleteTaskUseCaseImpl(taskRepository),
    private val getTaskImageByIdUseCase: GetTaskImageByIdUseCase = GetTaskImageByIdUseCaseImpl(taskRepository),
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase = DeleteTaskByIdUseCaseImpl(taskRepository),
) {

    suspend fun createTask(taskDto: TaskDto): TaskDto {
        val params = CreateTaskUseCaseParams(task = taskDto.asDomain)
        val createdTask = createTaskUseCase.execute(params = params)

        return createdTask.asDto
    }

    suspend fun listTasksByCompletionStatus(isCompleted: Boolean?): List<TaskDto> {
        val params = ListTasksUseCaseParams(isCompleted = isCompleted)
        val tasks = listTasksUseCase.execute(params = params)

        return tasks.map(Task::asDto)
    }

    suspend fun findTask(taskId: String): TaskDto? {
        val params = FindTaskUseCaseByIdParams(taskId)
        val task = findTaskUseCaseById.execute(params = params)

        return task?.asDto
    }

    suspend fun completeTask(taskId: String, fileData: FileData): TaskDto? {
        val params = CompleteTaskUseCaseParams(taskId, fileData)
        val task = completeTaskUseCase.execute(params = params)

        return task?.asDto
    }

    suspend fun getTaskImageById(taskId: String): File {
        val params = GetTaskImageByIdUseCaseParams(taskId = taskId)

        return getTaskImageByIdUseCase.execute(params = params)
    }

    suspend fun deleteTaskById(taskId: String): Boolean {
        val params = DeleteTaskByIdUseCaseParams(taskId)

        return deleteTaskByIdUseCase.execute(params = params)
    }
}
