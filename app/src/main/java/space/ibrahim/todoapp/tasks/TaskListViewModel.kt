package space.ibrahim.todoapp.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import space.ibrahim.todoapp.repository.ITaskRepository
import space.ibrahim.todoapp.repository.IUserRepository
import space.ibrahim.todoapp.util.Event

class TaskListViewModel @ViewModelInject constructor(
    private val taskRepository: ITaskRepository,
    private val userRepository: IUserRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<Event<UiState>>()
    val uiState: LiveData<Event<UiState>> = _uiState

    fun checkIsUserLoggedIn() {
        if (!userRepository.isUserLoggedIn()) _uiState.value = Event(UiState.UnauthorizedState)
    }

    fun getTasks() = viewModelScope.launch {
        _uiState.postValue(Event(UiState.LoadingState))
        try {
            val tasks = taskRepository.getTasks()
            _uiState.postValue(Event(UiState.TasksLoadedState(tasks)))
        } catch (e: Exception) {
            e.printStackTrace()
            _uiState.postValue(Event(UiState.ErrorState(e.localizedMessage ?: "Unknown error")))
        }
    }


    fun itemChecked(task: Task, position: Int, isChecked: Boolean) = viewModelScope.launch {
        try {
            val updatedTask = taskRepository.updateTask(task.copy(done = isChecked))
            _uiState.postValue(Event(UiState.TaskUpdatedState(updatedTask, position)))

        } catch (e: Exception) {
            _uiState.postValue(Event(UiState.ErrorState(e.localizedMessage ?: "Unknown error")))
        }
    }

    fun itemClicked(task: Task) {
        _uiState.postValue(Event(UiState.NavigateToTaskState(task)))
    }

    sealed class UiState {
        class TasksLoadedState(val tasks: List<Task>) : UiState()
        class TaskUpdatedState(val task: Task, val position: Int) : UiState()
        class NavigateToTaskState(val task: Task) : UiState()
        object UnauthorizedState : UiState()
        class ErrorState(val message: String) : UiState()
        object LoadingState : UiState()
    }

}
