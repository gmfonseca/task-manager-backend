package br.com.gmfonseca.taskmanager.data.repositories

import br.com.gmfonseca.taskmanager.data.datasources.TaskDataSource
import br.com.gmfonseca.taskmanager.data.datasources.TaskDataSourceImpl
import br.com.gmfonseca.taskmanager.domain.entities.Task
import br.com.gmfonseca.taskmanager.domain.repositories.TaskRepository
import br.com.gmfonseca.taskmanager.utils.mappers.asDomain
import br.com.gmfonseca.taskmanager.utils.mappers.asEntity
import br.com.gmfonseca.taskmanager.data.entities.Task as EntityTask

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource = TaskDataSourceImpl()
) : TaskRepository {

    override suspend fun createTask(task: Task): Task {
        val taskEntity = task.asEntity
        val createdTask = taskDataSource.createTask(taskEntity)

        return createdTask.asDomain
    }

    override suspend fun findTaskById(taskId: String): Task? {
        val task = taskDataSource.findTaskById(taskId)

        return task?.asDomain
    }

    override suspend fun updateTask(task: Task): Task? {
        val taskEntity = task.asEntity
        val updatedTask = taskDataSource.updateTask(taskEntity)

        return updatedTask?.asDomain
    }

    override suspend fun listTasks(): List<Task> {
        val tasks = taskDataSource.listTasks()

        return tasks.map(EntityTask::asDomain)
    }

    override suspend fun listTasksByCompletionStatus(isCompleted: Boolean): List<Task> {
        val tasks = taskDataSource.listTasksByCompletionStatus(isCompleted)

        return tasks.map(EntityTask::asDomain)
    }

    override suspend fun deleteTaskById(taskId: String): Boolean = taskDataSource.deleteTaskById(taskId)
}
