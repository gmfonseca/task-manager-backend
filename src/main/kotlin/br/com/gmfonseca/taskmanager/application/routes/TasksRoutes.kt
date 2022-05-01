package br.com.gmfonseca.taskmanager.application.routes

import br.com.gmfonseca.taskmanager.application.controllers.TaskController
import br.com.gmfonseca.taskmanager.application.dtos.TaskDto
import br.com.gmfonseca.taskmanager.utils.ext.mapNameToBytes
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.registerTaskRoutes() = routing { taskRouting() }

fun Route.taskRouting() = route("") {
    val taskController = TaskController()

    get {
        taskController.createTask(
            TaskDto(
                title = "Aowba!",
                description = "This is an awesome description dude! Lez gou..."
            )
        )
    }

    route("tasks") {

        listTasks(taskController)
        createTask(taskController)

        route("{id}") {
            findTask(taskController)
            deleteTask(taskController)
            completeTask(taskController)
            getImage(taskController)
        }
    }
}

private fun Route.listTasks(taskController: TaskController) {
    get {
        val completed = call.parameters["completed"]?.toBooleanStrictOrNull()

        val response = taskController.listTasksByCompletionStatus(isCompleted = completed)

        call.respond(response)
    }
}

private fun Route.findTask(taskController: TaskController) {
    get {
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

private fun Route.createTask(taskController: TaskController) {
    post {
        val task = call.receive<TaskDto>()

        val result = taskController.createTask(task)

        call.respond(HttpStatusCode.Created, result)
    }
}

private fun Route.deleteTask(taskController: TaskController) {
    delete {
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

private fun Route.completeTask(taskController: TaskController) {

    put {
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

private fun Route.getImage(taskController: TaskController) {
    get("image") {
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
