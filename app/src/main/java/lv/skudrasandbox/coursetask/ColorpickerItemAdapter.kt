package lv.skudrasandbox.coursetask

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.colorpicker_list_item.view.*

class ColorpickerItemAdapter(context: Context, items: List<ColorpickerItem>) :
    ArrayAdapter<ColorpickerItem>(context, 0, items) {

    data class ViewHolder(val colorName: TextView, val cardPreview: CardView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        lateinit var viewHolder: ViewHolder
        lateinit var view: View

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.colorpicker_list_item, parent, false)
            viewHolder = ViewHolder(view.colorName, view.cardMiniPreview)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        val item = getItem(position)!!

        viewHolder.colorName.text = item.name
        viewHolder.cardPreview.setCardBackgroundColor(Color.parseColor(item.color))

        return view
}}