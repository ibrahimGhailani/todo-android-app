package space.ibrahim.todoapp.network

import retrofit2.Response
import retrofit2.http.*

interface UserRemoteSource {

    @GET("/api/v1/user")
    suspend fun login(
        @Header("Authorization") basicAuth: String
    ): Response<Void>

    @POST("/api/v1/user")
    @FormUrlEncoded
    suspend fun createUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<Void>
}