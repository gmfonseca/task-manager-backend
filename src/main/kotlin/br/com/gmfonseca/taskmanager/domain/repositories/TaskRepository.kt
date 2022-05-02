package br.com.gmfonseca.taskmanager.domain.repositories

import br.com.gmfonseca.taskmanager.domain.entities.Task

interface TaskRepository {

    /**
     * Save the given [task] in the storage and return itself as result
     */
    suspend fun createTask(task: Task): Task

    /**
     * Find the first task with the [Task.id] that matches the given [taskId]
     *
     * @param taskId the desired task id
     *
     * @return the found task or NULL if didn't find any
     */
    suspend fun findTaskById(taskId: String): Task?

    /**
     * Update an existing task with the given [task] values.
     * If the [task] doesn't exist then nothing happens.
     *
     * @param task existing task with updated values
     *
     * @return the updated task or NULL if the given [task] doesn't exist
     */
    suspend fun updateTask(task: Task): Task?

    /**
     * List all tasks
     *
     * @return the list of all existing tasks
     */
    suspend fun listTasks(): List<Task>

    /**
     * List all tasks with the [Task.isCompleted] that matches the given [isCompleted]
     *
     * @param isCompleted the desired completion status to filter in
     *
     * @return the list of tasks filtered by the completion status
     */
    suspend fun listTasksByCompletionStatus(isCompleted: Boolean): List<Task>

    /**
     * Delete the first task with the [Task.id] that matches the given [taskId]
     *
     * @param taskId the id of the desired task to be deleted
     *
     * @return true if successfully deleted, or false otherwise
     */
    suspend fun deleteTaskById(taskId: String): Boolean
}