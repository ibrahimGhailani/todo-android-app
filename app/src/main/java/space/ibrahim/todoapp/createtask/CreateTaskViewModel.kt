package space.ibrahim.todoapp.createtask

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import space.ibrahim.todoapp.repository.TaskRepository
import space.ibrahim.todoapp.util.Event

class CreateTaskViewModel @ViewModelInject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<Event<UiState>>()
    val uiState: LiveData<Event<UiState>> = _uiState

    fun createTask(title: String, content: String) = try {
        viewModelScope.launch {
            taskRepository.createTask(title, content)
            _uiState.postValue(Event(UiState.TaskCreated))
        }
    } catch (e: Exception) {
        _uiState.postValue(Event(UiState.ErrorState(e.localizedMessage ?: "Unknown Error")))
    }


    sealed class UiState {
        object UnauthorizedState : UiState()
        class ErrorState(val message: String) : UiState()
        object TaskCreated : UiState()
    }
}
