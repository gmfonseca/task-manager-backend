package br.com.gmfonseca.taskmanager.data.datasources

import br.com.gmfonseca.taskmanager.data.entities.Task

interface TaskDataSource {

    suspend fun createTask(task: Task): Task

    suspend fun findTask(taskId: String): Task?

    suspend fun updateTask(task: Task): Task?

    suspend fun listTasks(): List<Task>

    suspend fun listTasksByCompletionStatus(isCompleted: Boolean): List<Task>

    suspend fun deleteTaskById(taskId: String): Boolean
}