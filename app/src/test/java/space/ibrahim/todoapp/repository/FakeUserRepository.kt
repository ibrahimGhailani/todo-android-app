package space.ibrahim.todoapp.repository

import space.ibrahim.todoapp.repository.IUserRepository

class FakeUserRepository: IUserRepository {
    override suspend fun createUser(username: String, password: String) {
        return
    }

    override suspend fun login(username: String, password: String) {
        return
    }

    override fun isUserLoggedIn(): Boolean = true
}