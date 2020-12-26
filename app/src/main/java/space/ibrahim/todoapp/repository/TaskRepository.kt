package space.ibrahim.todoapp.repository

import space.ibrahim.todoapp.network.TaskRemoteSource
import space.ibrahim.todoapp.tasks.Task
import space.ibrahim.todoapp.util.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskRemoteSource: TaskRemoteSource,
    private val preferenceManager: PreferenceManager
) : ITaskRepository {
    override suspend fun getTasks(): List<Task> {
        val response = taskRemoteSource.getAllTasks()
        if (!response.isSuccessful) throw Exception("Something went wrong")
        return response.body() ?: throw Exception("Something went wrong")
    }

    override suspend fun updateTask(task: Task): Task {
        val response = taskRemoteSource.updateTask(task.id, task)
        if (!response.isSuccessful) throw Exception("Something went wrong")
        return response.body() ?: throw Exception("Something went wrong")
    }

    override suspend fun createTask(title: String, content: String): Task {
        val response = taskRemoteSource.createTask(
            Task(
                title = title,
                content = content
            )
        )
        if (!response.isSuccessful) throw Exception("Something went wrong")
        return response.body() ?: throw Exception("Something went wrong")
    }
}