package space.ibrahim.todoapp.user

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.ibrahim.todoapp.repository.IUserRepository
import space.ibrahim.todoapp.util.Event

class RegistrationViewModel @ViewModelInject constructor(
    private val userRepository: IUserRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<Event<UiState>>()
    val uiState: LiveData<Event<UiState>> = _uiState

    fun register(username: String, password: String, alreadyRegistered: Boolean = false) {
        if (username.isBlank()) {
            _uiState.postValue(Event(UiState.ErrorState("Missing Username")))
            return
        }
        if (password.isBlank()) {
            _uiState.postValue(Event(UiState.ErrorState("Missing Password")))
            return
        }

        viewModelScope.launch {
            try {
                if (alreadyRegistered) userRepository.login(username, password)
                else userRepository.createUser(username, password)
                _uiState.postValue(Event(UiState.RegistrationSucceededState))
            } catch (e: Exception) {
                _uiState.postValue(Event(UiState.ErrorState(e.localizedMessage ?: "Unknown Error")))
            }
        }
    }

    sealed class UiState {
        class ErrorState(val message: String) : UiState()
        object RegistrationSucceededState : UiState()
    }
}