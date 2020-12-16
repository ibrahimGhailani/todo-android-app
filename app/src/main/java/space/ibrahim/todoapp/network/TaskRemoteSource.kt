package space.ibrahim.todoapp.network

import retrofit2.Response
import retrofit2.http.*
import space.ibrahim.todoapp.tasks.Task

interface TaskRemoteSource {

    @GET("/api/v1/task")
    suspend fun getAllTasks(): Response<List<Task>>

    @POST("/api/v1/task")
    suspend fun createTask(@Body task: Task): Response<Task>

    fun deleteTask() {
        TODO("Not yet implemented")
    }

    @PUT("/api/v1/task/{id}")
    suspend fun updateTask(
        @Path("id") id: Long,
        @Body task: Task
    ): Response<Task>
}