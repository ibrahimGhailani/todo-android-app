package space.ibrahim.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import space.ibrahim.todoapp.TestCoroutineRule
import space.ibrahim.todoapp.getOrAwaitValue
import space.ibrahim.todoapp.repository.FakeTaskRepository
import space.ibrahim.todoapp.repository.FakeUserRepository
import space.ibrahim.todoapp.tasks.TaskListViewModel.UiState.NavigateToTaskState
import space.ibrahim.todoapp.tasks.TaskListViewModel.UiState.TasksLoadedState

@ExperimentalCoroutinesApi
class TaskListViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = TestCoroutineRule()

    private val taskRepository = FakeTaskRepository()

    private val viewModel = TaskListViewModel(
        taskRepository = taskRepository,
        userRepository = FakeUserRepository()
    )

    @Test
    fun `When tasks are saved, load tasks in UI`() =
        mainCoroutineRule.dispatcher.runBlockingTest {
            taskRepository.createTask(title = "1", content = "content 1")
            taskRepository.createTask(title = "2", content = "content 2")

            viewModel.getTasks()

            val uiState = viewModel.uiState.getOrAwaitValue().getContentIfNotHandled()
            assert(uiState is TasksLoadedState)
            assert((uiState as TasksLoadedState).tasks.size == 2)

        }

    @Test
    fun `When task is marked done, notify that the task is done`() =
        mainCoroutineRule.dispatcher.runBlockingTest {
            val task = taskRepository.createTask("1", "content 1")

            viewModel.itemChecked(task = task, position = 0, isChecked = true)
            val uiState = viewModel.uiState.getOrAwaitValue()
                .getContentIfNotHandled() as TaskListViewModel.UiState.TaskUpdatedState

            assert(uiState.task.done)
        }

    @Test
    fun `When task is marked undone, notify UI that the task is not done`() =
        mainCoroutineRule.dispatcher.runBlockingTest {
            val task = taskRepository.createTask("1", "content 1")

            viewModel.itemChecked(task = task, position = 0, isChecked = false)

            val uiState = viewModel.uiState.getOrAwaitValue()
                .getContentIfNotHandled() as TaskListViewModel.UiState.TaskUpdatedState
            assert(!uiState.task.done)
        }

    @Test
    fun `When item is clicked, navigate to item details`() =
        mainCoroutineRule.dispatcher.runBlockingTest {
            val task = taskRepository.createTask("1", "content 1")

            viewModel.itemClicked(task)
            val uiState = viewModel.uiState.getOrAwaitValue()
                .getContentIfNotHandled()

            assert(uiState is NavigateToTaskState)
            assert((uiState as NavigateToTaskState).task == task)
        }
}