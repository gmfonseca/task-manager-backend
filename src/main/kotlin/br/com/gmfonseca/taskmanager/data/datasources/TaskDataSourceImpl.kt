package br.com.gmfonseca.taskmanager.data.datasources

import br.com.gmfonseca.taskmanager.data.entities.Task

class TaskDataSourceImpl : TaskDataSource {

    private val _taskStorage: MutableList<Task> = mutableListOf()

    override suspend fun createTask(task: Task): Task = task.apply { _taskStorage.add(task) }

    override suspend fun findTask(taskId: String): Task? = _taskStorage.find { it.id == taskId }

    override suspend fun updateTask(task: Task): Task? {
        return if (deleteTaskById(task.id)) {
            createTask(task)
        } else null
    }

    override suspend fun listTasks(): List<Task> = _taskStorage

    override suspend fun listTasksByCompletionStatus(isCompleted: Boolean): List<Task> =
        _taskStorage.filter { it.isCompleted == isCompleted }

    override suspend fun deleteTaskById(taskId: String) = _taskStorage.removeIf { it.id == taskId }
}