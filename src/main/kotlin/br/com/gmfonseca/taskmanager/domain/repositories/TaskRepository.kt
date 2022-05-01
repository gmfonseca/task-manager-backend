package br.com.gmfonseca.taskmanager.domain.repositories

import br.com.gmfonseca.taskmanager.domain.entities.Task

interface TaskRepository {
    suspend fun createTask(task: Task): Task

    suspend fun findTaskById(taskId: String): Task?

    suspend fun updateTask(task: Task): Task?

    suspend fun listTasks(): List<Task>

    suspend fun listTasksByCompletionStatus(isCompleted: Boolean): List<Task>

    suspend fun deleteTaskById(taskId: String): Boolean
}