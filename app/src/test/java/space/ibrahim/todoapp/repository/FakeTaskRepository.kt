package space.ibrahim.todoapp.repository

import space.ibrahim.todoapp.tasks.Task
import kotlin.random.Random

class FakeTaskRepository : ITaskRepository {

    private val tasks = mutableListOf<Task>()

    override suspend fun getTasks(): List<Task> {
        return tasks
    }

    override suspend fun updateTask(task: Task): Task {
        val index = tasks.indexOfFirst { it.id == task.id }
        tasks[index] = task
        return tasks[index]
    }

    override suspend fun createTask(title: String, content: String): Task {
        val task = Task(
            id = Random.nextLong(),
            title = title,
            content = content
        )
        tasks.add(task)
        return task
    }
}