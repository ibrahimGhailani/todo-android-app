package space.ibrahim.todoapp.repository

import space.ibrahim.todoapp.tasks.Task

interface ITaskRepository {
    suspend fun getTasks(): List<Task>

    suspend fun updateTask(task: Task): Task

    suspend fun createTask(title: String, content: String): Task
}