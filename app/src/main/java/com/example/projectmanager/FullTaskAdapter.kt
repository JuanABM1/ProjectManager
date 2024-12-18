import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.R
import com.example.projectmanager.Task

class FullTaskAdapter(
    private val taskList: MutableList<Task>,
    private val onStateChanged: (Task) -> Unit
) : RecyclerView.Adapter<FullTaskAdapter.FullTaskViewHolder>() {

    class FullTaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskName: TextView = view.findViewById(R.id.taskName)
        val taskDescription: TextView = view.findViewById(R.id.taskDescription)
        val taskStateSpinner: Spinner = view.findViewById(R.id.taskStateSpinner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullTaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.full_task_information_layout, parent, false)
        return FullTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: FullTaskViewHolder, position: Int) {
        val task = taskList[position]

        holder.taskName.text = task.nombre_tarea
        holder.taskDescription.text = task.descripcion

        // Configurar el Spinner
        val adapter = ArrayAdapter.createFromResource(
            holder.itemView.context,
            R.array.task_states,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.taskStateSpinner.adapter = adapter

        // Establecer el estado actual en el Spinner
        val currentIndex = adapter.getPosition(task.estado)
        holder.taskStateSpinner.setSelection(currentIndex)

        // Manejar cambios en el Spinner
        holder.taskStateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val newState = parent?.getItemAtPosition(position).toString()
                if (task.estado != newState) {
                    task.estado = newState
                    onStateChanged(task) // Notificar cambio
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun getItemCount(): Int = taskList.size
}
