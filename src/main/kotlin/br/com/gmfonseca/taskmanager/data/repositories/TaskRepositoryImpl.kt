package br.com.gmfonseca.taskmanager.data.repositories

import br.com.gmfonseca.taskmanager.data.datasources.TaskDataSource
import br.com.gmfonseca.taskmanager.data.datasources.TaskDataSourceImpl
import br.com.gmfonseca.taskmanager.domain.entities.Task
import br.com.gmfonseca.taskmanager.domain.repositories.TaskRepository
import br.com.gmfonseca.taskmanager.utils.mappers.asDomain
import br.com.gmfonseca.taskmanager.utils.mappers.asEntity
import br.com.gmfonseca.taskmanager.data.entities.Task as EntityTask

class TaskRepositoryImpl private constructor(
    private val taskDataSource: TaskDataSource
) : TaskRepository {

    override suspend fun createTask(task: Task): Task = taskDataSource.createTask(task.asEntity).asDomain

    override suspend fun findTaskById(taskId: String): Task? = taskDataSource.findTask(taskId)?.asDomain

    override suspend fun updateTask(task: Task): Task? = taskDataSource.updateTask(task.asEntity)?.asDomain

    override suspend fun listTasks(): List<Task> = taskDataSource.listTasks().map(EntityTask::asDomain)

    override suspend fun listTasksByCompletionStatus(isCompleted: Boolean): List<Task> =
        taskDataSource.listTasksByCompletionStatus(isCompleted).map(EntityTask::asDomain)

    override suspend fun deleteTaskById(taskId: String): Boolean = taskDataSource.deleteTaskById(taskId)

    companion object : RepositoryFactory<TaskRepositoryImpl> {
        private val INSTANCE by lazy { TaskRepositoryImpl(TaskDataSourceImpl()) }

        override fun invoke(): TaskRepositoryImpl = INSTANCE
    }
}
