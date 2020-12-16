package space.ibrahim.todoapp.tasks

data class Task(
    val id: Long = -1,
    val title: String,
    val content: String,
    val done: Boolean = false
)