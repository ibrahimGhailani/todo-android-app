package space.ibrahim.todoapp.repository

interface IUserRepository {
    suspend fun createUser(username: String, password: String)

    suspend fun login(username: String, password: String)

    fun isUserLoggedIn(): Boolean
}