package lv.skudrasandbox.coursetask

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_item.view.*
import lv.skudrasandbox.coursetask.items.NoteData
import lv.skudrasandbox.coursetask.screens.main.AdapterClickListener

class NoteRecyclerAdapter(
    private val listener: AdapterClickListener,
    private val notes: MutableList<NoteData>
) :
    RecyclerView.Adapter<NoteRecyclerAdapter.ShoppingViewHolder>() {

    class ShoppingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return ShoppingViewHolder(view)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val note = notes[position]
        val context = holder.itemView.context
        holder.itemView.noteViewTitle.text = note.title
        holder.itemView.importanceFlagImage.visibility = if (note.is_important) View.VISIBLE else View.GONE
        holder.itemView.noteCardView.setCardBackgroundColor(Color.parseColor(note.color))
        holder.itemView.noteViewTextLines.text = note.json

        holder.itemView.setOnClickListener {
            listener.itemClicked(notes[position])
        }

        holder.itemView.deleteNoteButton.setOnClickListener {
            listener.deleteClicked(notes[position])
        }
    }
}