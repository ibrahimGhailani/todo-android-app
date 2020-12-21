package space.ibrahim.todoapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import space.ibrahim.todoapp.BuildConfig
import space.ibrahim.todoapp.network.TaskRemoteSource
import space.ibrahim.todoapp.network.UserRemoteSource
import space.ibrahim.todoapp.util.PreferenceManager
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG)
            logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }


    @Named("BasicAuth")
    @Provides
    @Singleton
    fun provideBasicAuthInterceptor(preferenceManager: PreferenceManager): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val authenticatedRequest = preferenceManager.getBasicAuth()?.let { credentials ->
                request.newBuilder()
                    .addHeader("Authorization", credentials)
                    .build()
            } ?: request
            chain.proceed(authenticatedRequest)
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @Named("BasicAuth") basicAuthInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(basicAuthInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl("https://todo-spring1.herokuapp.com")
            .build()
    }


    @Provides
    @Singleton
    fun provideTaskRemoteSource(retrofit: Retrofit): TaskRemoteSource {
        return retrofit.create(TaskRemoteSource::class.java)
    }


    @Singleton
    @Provides
    fun provideUserRemoteSource(retrofit: Retrofit): UserRemoteSource {
        return retrofit.create(UserRemoteSource::class.java)
    }
}