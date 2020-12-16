package space.ibrahim.todoapp.repository

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Credentials
import space.ibrahim.todoapp.network.UserRemoteSource
import space.ibrahim.todoapp.util.PreferenceManager
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val remoteSource: UserRemoteSource,
    private val preferenceManager: PreferenceManager
) {

    suspend fun createUser(username: String, password: String) {
        val response = remoteSource.createUser(username, password)
        preferenceManager.saveBasicAuth(username, password)
        if (!response.isSuccessful) throw Exception("Something went wrong")
    }

    suspend fun login(username: String, password: String) {
        val basicAuth = Credentials.basic(username, password)
        val response = remoteSource.login(basicAuth)
        preferenceManager.saveBasicAuth(username, password)
        if (!response.isSuccessful) throw Exception("Something went wrong")
    }

    fun isUserLoggedIn(): Boolean {
        return preferenceManager.getBasicAuth() != null
    }

    companion object {
        const val TAG = "UserRepository"
    }
}