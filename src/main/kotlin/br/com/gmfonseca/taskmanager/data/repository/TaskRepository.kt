package br.com.gmfonseca.taskmanager.data.repository

import br.com.gmfonseca.taskmanager.model.Task

class TaskRepository private constructor() {

    private val taskStorage = mutableListOf<Task>(
        Task(
            title = "Murilo Couto - Juceclino Kubicast",
            description = "Defante Shop - https://defanteshop.com.br/"
        ),
        Task(
            title = "Estudar figma",
            description = "Criar layouts"
        ),
    )

    fun findTask(taskId: String): Task? = taskStorage.find { it.id == taskId }

    companion object : RepositoryFactory<TaskRepository> {
        private val INSTANCE by lazy { TaskRepository() }

        override fun invoke(): TaskRepository = INSTANCE
    }
}
