package br.com.gmfonseca.taskmanager.application.controllers

import br.com.gmfonseca.taskmanager.data.repository.TaskRepository
import br.com.gmfonseca.taskmanager.model.Task

class TaskController(private val taskRepository: TaskRepository = TaskRepository()) {

    fun findTask(taskId: String): Task? = taskRepository.findTask(taskId)

}
