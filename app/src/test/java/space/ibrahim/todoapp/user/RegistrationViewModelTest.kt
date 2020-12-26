package space.ibrahim.todoapp.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import space.ibrahim.todoapp.TestCoroutineRule
import space.ibrahim.todoapp.getOrAwaitValue
import space.ibrahim.todoapp.repository.FakeUserRepository
import space.ibrahim.todoapp.user.RegistrationViewModel.UiState.ErrorState

@ExperimentalCoroutinesApi
class RegistrationViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = TestCoroutineRule()

    private val viewModel = RegistrationViewModel(
        FakeUserRepository()
    )

    @Test
    fun `When username is empty, throw exception`() {
        viewModel.register("", "password")
        val uiState = viewModel.uiState.getOrAwaitValue().getContentIfNotHandled()

        assert(uiState is ErrorState)
        assert((uiState as ErrorState).message == "Missing Username")
    }

    @Test
    fun `When password is empty, throw exception`() {
        viewModel.register("username", "")
        val uiState = viewModel.uiState.getOrAwaitValue().getContentIfNotHandled()

        assert(uiState is ErrorState)
        assert((uiState as ErrorState).message == "Missing Password")
    }

    @Test
    fun `When username and password filled, register new user`() {
        mainCoroutineRule.dispatcher.runBlockingTest {
            viewModel.register("username", "password")
            val uiState = viewModel.uiState.getOrAwaitValue().getContentIfNotHandled()

            assert(uiState is RegistrationViewModel.UiState.RegistrationSucceededState)
        }
    }
}