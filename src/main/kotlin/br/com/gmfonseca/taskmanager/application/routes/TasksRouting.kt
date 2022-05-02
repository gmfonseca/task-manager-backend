package br.com.gmfonseca.taskmanager.application.routes

import br.com.gmfonseca.taskmanager.application.controllers.TaskController
import br.com.gmfonseca.taskmanager.application.dtos.TaskDto
import br.com.gmfonseca.taskmanager.utils.contracts.HttpMethod
import br.com.gmfonseca.taskmanager.utils.contracts.rest.RoutePath
import br.com.gmfonseca.taskmanager.utils.ext.extractFileData
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*

class TasksRouting(
    application: Application,
    private val taskController: TaskController = TaskController()
) {

    init {
        application.tasksRouting()
    }

    private fun Application.tasksRouting() {
        routing {
            route("tasks") {
                post { createTask() }
                get { listTasks() }

                get("{id}") { findTask() }
                put("{id}") { completeTask() }
                delete("{id}") { deleteTask() }

                get("{id}/image") { getImage() }
            }
        }
    }

    @RoutePath(name = "tasks", method = HttpMethod.POST)
    private suspend fun PipelineContext<Unit, ApplicationCall>.createTask() {
        val task = call.receive<TaskDto>()

        val createdTask = taskController.createTask(task)

        call.respond(HttpStatusCode.Created, createdTask)
    }

    @RoutePath(name = "tasks", method = HttpMethod.GET)
    private suspend fun PipelineContext<Unit, ApplicationCall>.listTasks() {
        val completed = call.parameters["completed"]?.toBooleanStrictOrNull()

        val response = taskController.listTasksByCompletionStatus(isCompleted = completed)

        call.respond(response)
    }

    @RoutePath(name = "tasks/{id}", method = HttpMethod.GET)
    private suspend fun PipelineContext<Unit, ApplicationCall>.findTask() {
        val id = call.getId()

        val task = taskController.findTask(id)
            ?: return call.respond(
                status = HttpStatusCode.NotFound,
                message = "Task not found"
            )

        call.respond(task)
    }

    @RoutePath(name = "tasks/{id}", method = HttpMethod.PUT)
    private suspend fun PipelineContext<Unit, ApplicationCall>.completeTask() {
        val id = call.getId()

        try {
            val fileData = call.receiveMultipart().extractFileData()

            val task = taskController.completeTask(taskId = id, fileData)
                ?: return call.respond(
                    status = HttpStatusCode.NotFound,
                    message = "Task not found"
                )

            call.respond(task)
        } catch (_: UninitializedPropertyAccessException) {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Missing upload file"
            )
        }
    }

    @RoutePath(name = "tasks/{id}", method = HttpMethod.DELETE)
    private suspend fun PipelineContext<Unit, ApplicationCall>.deleteTask() {
        val id = call.getId()
        val deletedTask = taskController.deleteTaskById(id)

        if (deletedTask) call.respond(HttpStatusCode.OK)
        else call.respond(status = HttpStatusCode.NotFound, message = "Task not found")
    }

    @RoutePath(name = "tasks/{id}/image", method = HttpMethod.GET)
    private suspend fun PipelineContext<Unit, ApplicationCall>.getImage() {
        val id = call.getId()

        try {
            val imageFile = taskController.getTaskImageById(id)

            call.respondFile(imageFile)
        } catch (e: NotFoundException) {
            call.respond(status = HttpStatusCode.NotFound, "${e.message}")
        }
    }

    private fun ApplicationCall.requireParameter(name: String, lazyMessage: () -> String): String {
        return requireNotNull(parameters[name]) {
            throw BadRequestException(message = lazyMessage())
        }
    }

    private fun ApplicationCall.getId(): String = requireParameter("id") { "Missing or malformed id" }
}
