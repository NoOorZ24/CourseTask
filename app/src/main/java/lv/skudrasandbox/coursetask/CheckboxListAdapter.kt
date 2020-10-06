package lv.skudrasandbox.coursetask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.checkbox_list_item.view.*

class CheckboxListAdapter(private val items: MutableList<CheckboxItem>) :
    RecyclerView.Adapter<CheckboxListAdapter.CheckboxListViewHolder>() {
    class CheckboxListViewHolder(view: View) : RecyclerView.ViewHolder(view)

    // inflate layout and create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckboxListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.checkbox_list_item, parent, false)
        return CheckboxListViewHolder(view)
    }

    // need to return item count
    override fun getItemCount() = items.size
    // bind item to view holder
    override fun onBindViewHolder(holder: CheckboxListViewHolder, position: Int) {
//        val item = items[position]
//        val context = holder.itemView.context
//        holder.itemView.itemDeleteButton.setOnClickListener {
//            Toast.makeText(context, "${item.text} was removed", Toast.LENGTH_SHORT).show()
//            items.removeAt(position)
//            notifyDataSetChanged()
//        }
    }
}
