package br.com.gmfonseca.taskmanager.application.routes

import br.com.gmfonseca.taskmanager.model.Task
import br.com.gmfonseca.taskmanager.model.taskStorage
import br.com.gmfonseca.taskmanager.utils.ext.mapNameToBytes
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route
import io.ktor.routing.routing
import java.io.File

fun Application.registerTaskRoutes() = routing { taskRouting() }

fun Route.taskRouting() = route("tasks") {
    listTasks()
    findTask()
    saveTask()
    deleteTask()
    completeTask()
}

private fun Route.listTasks() {
    get {
        if (taskStorage.isEmpty()) {
            call.respond(
                status = HttpStatusCode.OK,
                message = "No tasks registered."
            )
        } else {
            val completed = call.parameters["completed"]?.toBooleanStrictOrNull()

            val response = if (completed != null) {
                taskStorage.filter { it.isCompleted == completed }
            } else {
                taskStorage
            }

            if (response.isEmpty()) {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = "No tasks registered."
                )
            } else {
                call.respond(response)
            }
        }
    }
}

private fun Route.findTask() {
    get("{id}") {
        val id = call.parameters["id"] ?: return@get call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )

        val task = taskStorage.find { it.id == id } ?: return@get call.respond(
            status = HttpStatusCode.NotFound,
            message = "Task not found."
        )

        call.respond(task)
    }
}

private fun Route.saveTask() {
    post {
        val task = call.receive<Task>()

        taskStorage.add(task)

        call.respond(HttpStatusCode.Created, task)
    }
}

private fun Route.deleteTask() {
    delete("{id}") {
        val id = call.parameters["id"] ?: return@delete call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )

        if (taskStorage.removeIf { it.id == id }) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(
                status = HttpStatusCode.NotFound,
                message = "Task not found."
            )
        }
    }
}

private fun Route.completeTask() {
    val uploadsDir = File("uploads")

    if (!uploadsDir.exists()) {
        uploadsDir.mkdirs()
    }

    put("{id}") {
        val id = call.parameters["id"] ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )

        try {
            val (fileName, fileBytes) = call.receiveMultipart().mapNameToBytes()

            val task = taskStorage.find { it.id == id } ?: return@put call.respond(
                status = HttpStatusCode.NotFound,
                message = "Task not found."
            )

            val file: File = File(uploadsDir, fileName).also { it.writeBytes(fileBytes) }

            if (!task.isCompleted) {
                task.isCompleted = true
            } else {
                task.imagePath?.let { File(it).delete() }
            }

            task.imagePath = file.path

            call.respond(task)
        } catch (_: UninitializedPropertyAccessException) {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Missing upload file"
            )
        }
    }
}
