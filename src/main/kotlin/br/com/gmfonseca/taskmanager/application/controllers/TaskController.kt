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

class TaskController(
    taskRepository: TaskRepository = TaskRepositoryImpl(),
    private val findTaskUseCaseById: FindTaskUseCaseById = FindTaskUseCaseByIdImpl(taskRepository),
    private val listTasksUseCase: ListTasksUseCase = ListTasksUseCaseImpl(taskRepository),
    private val createTaskUseCase: CreateTaskUseCase = CreateTaskUseCaseImpl(taskRepository),
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase = DeleteTaskByIdUseCaseImpl(taskRepository),
    private val completeTaskUseCase: CompleteTaskUseCase = CompleteTaskUseCaseImpl(taskRepository),
    private val getTaskImageByIdUseCase: GetTaskImageByIdUseCase = GetTaskImageByIdUseCaseImpl(taskRepository),
) {

    suspend fun createTask(dto: TaskDto): TaskDto = createTaskUseCase.execute(
        params = CreateTaskUseCaseParams(task = dto.asDomain)
    ).asDto

    suspend fun findTask(taskId: String): TaskDto? = findTaskUseCaseById.execute(
        params = FindTaskUseCaseByIdParams(taskId)
    )?.asDto

    suspend fun listTasksByCompletionStatus(isCompleted: Boolean?): List<TaskDto> =
        listTasksUseCase.execute(
            params = ListTasksUseCaseParams(isCompleted = isCompleted)
        ).map(Task::asDto)

    suspend fun deleteTaskById(taskId: String) = deleteTaskByIdUseCase.execute(
        params = DeleteTaskByIdUseCaseParams(taskId)
    )

    suspend fun completeTask(taskId: String, fileName: String, fileBytes: ByteArray): TaskDto? =
        completeTaskUseCase.execute(
            params = CompleteTaskUseCaseParams(taskId, fileName, fileBytes)
        )?.asDto

    suspend fun getTaskImageById(taskId: String) = getTaskImageByIdUseCase.execute(
        params = GetTaskImageByIdUseCaseParams(taskId = taskId)
    )
}
