package br.com.gmfonseca.taskmanager.application.routes

import br.com.gmfonseca.taskmanager.application.controllers.TaskController
import br.com.gmfonseca.taskmanager.application.dtos.TaskDto
import br.com.gmfonseca.taskmanager.contracts.HttpMethod
import br.com.gmfonseca.taskmanager.contracts.rest.RoutePath
import br.com.gmfonseca.taskmanager.utils.ext.mapNameToBytes
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

class TasksRouting(
    application: Application,
    private val taskController: TaskController = TaskController()
) {

    init {
        application.routing {
            route("tasks") {
                listTasks()
                createTask()

                findTask()
                deleteTask()
                completeTask()
                getImage()
            }
        }
    }

    @RoutePath(name = "tasks", method = HttpMethod.GET)
    private fun Route.listTasks() {
        get {
            val completed = call.parameters["completed"]?.toBooleanStrictOrNull()

            val response = taskController.listTasksByCompletionStatus(isCompleted = completed)

            call.respond(response)
        }
    }

    @RoutePath(name = "tasks/{id}", method = HttpMethod.GET)
    private fun Route.findTask() {
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Missing or malformed id"
            )

            val task = taskController.findTask(id) ?: return@get call.respond(
                status = HttpStatusCode.NotFound,
                message = "Task not found."
            )

            call.respond(task)
        }
    }

    @RoutePath(name = "tasks", method = HttpMethod.POST)
    private fun Route.createTask() {
        post {
            val task = call.receive<TaskDto>()

            val result = taskController.createTask(task)

            call.respond(HttpStatusCode.Created, result)
        }
    }

    @RoutePath(name = "tasks/{id}", method = HttpMethod.DELETE)
    private fun Route.deleteTask() {
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Missing or malformed id"
            )

            if (taskController.deleteTaskById(id)) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(
                    status = HttpStatusCode.NotFound,
                    message = "Task not found."
                )
            }
        }
    }

    @RoutePath(name = "tasks/{id}", method = HttpMethod.PUT)
    private fun Route.completeTask() {
        put("{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Missing or malformed id"
            )

            try {
                val (fileName, fileBytes) = call.receiveMultipart().mapNameToBytes()

                val task = taskController.completeTask(taskId = id, fileName, fileBytes)
                    ?: return@put call.respond(
                        status = HttpStatusCode.NotFound,
                        message = "Task not found."
                    )

                call.respond(task)
            } catch (_: UninitializedPropertyAccessException) {
                println("[CompleteTask] Missing upload file")
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = "Missing upload file"
                )
            }
        }
    }

    @RoutePath(name = "tasks/{id}/image", method = HttpMethod.GET)
    private fun Route.getImage() {
        get("{id}/image") {
            val id = call.parameters["id"] ?: return@get call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Missing or malformed id"
            )

            try {
                val image = taskController.getTaskImageById(id) ?: return@get call.respond(
                    status = HttpStatusCode.NotFound,
                    message = "Image not found."
                )

                call.respondFile(image)
            } catch (e: NotFoundException) {
                call.respond(status = HttpStatusCode.NotFound, "${e.message}")
            }
        }
    }
}
