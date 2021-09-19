package br.com.gmfonseca.taskmanager.routes

import br.com.gmfonseca.taskmanager.model.Task
import br.com.gmfonseca.taskmanager.model.taskStorage
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route
import io.ktor.routing.routing

fun Application.registerTaskRoutes() = routing { taskRouting() }

fun Route.taskRouting() = route("tasks") {
    listTasks()
    findTask()
    saveTask()
    completeTask()
}

private fun Route.listTasks() {
    get {
        if (taskStorage.isEmpty()) {
            call.respond(
                status = HttpStatusCode.NotFound,
                message = "No tasks registered."
            )
        } else {
            call.respond(taskStorage)
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

private fun Route.completeTask() {
    put("{id}/complete") {
        val id = call.parameters["id"] ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )

        val task = taskStorage.find { it.id == id } ?: return@put call.respond(
            status = HttpStatusCode.NotFound,
            message = "Task not found."
        )

        if (!task.isCompleted) {
            task.isCompleted = true
        }

        call.respond(task)
    }
}
