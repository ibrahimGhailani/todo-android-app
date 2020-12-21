package space.ibrahim.todoapp.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import space.ibrahim.todoapp.databinding.ItemTaskBinding

class TaskAdapter(
    private val tasks: MutableList<Task> = mutableListOf(),
    val doOnTaskChecked: (Task, Int, Boolean) -> Unit,
    val doOnItemClicked: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position], position)
    }

    override fun getItemCount(): Int = tasks.size

    fun insertTasks(tasks: List<Task>) {
        this.tasks.clear()
        this.tasks.addAll(tasks)
        notifyDataSetChanged()
    }

    fun updateTask(task: Task, position: Int) {
        this.tasks[position] = task
        notifyItemChanged(position)
    }

    inner class TaskViewHolder(private val item: ItemTaskBinding) :
        RecyclerView.ViewHolder(item.root) {

        fun bind(task: Task, position: Int) {
            item.title.text = task.title
            item.content.text = task.content
            item.done.isChecked = task.done

            item.done.setOnClickListener {
                doOnTaskChecked(task, position, item.done.isChecked)
            }
            item.root.setOnClickListener {
                doOnItemClicked(task)
            }
        }

    }

}
