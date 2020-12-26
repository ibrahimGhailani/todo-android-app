package space.ibrahim.todoapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import space.ibrahim.todoapp.repository.ITaskRepository
import space.ibrahim.todoapp.repository.IUserRepository
import space.ibrahim.todoapp.repository.TaskRepository
import space.ibrahim.todoapp.repository.UserRepository

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUserRepository(
        userRepository: UserRepository
    ): IUserRepository

    @Binds
    abstract fun bindTaskRepository(
        taskRepository: TaskRepository
    ): ITaskRepository
}