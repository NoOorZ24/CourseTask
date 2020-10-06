package lv.skudrasandbox.coursetask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.PopupMenu
import kotlinx.android.synthetic.main.activity_create_note.*
import android.app.AlertDialog
import android.graphics.Color
import android.view.View

class CreateNoteActivity : AppCompatActivity() {
    private val checkboxList = mutableListOf<CheckboxItem>()
    private lateinit var adapter: CheckboxListAdapter

    private val colorpickerList = mutableListOf<ColorpickerItem>(
        ColorpickerItem("Yellow", "#F1E8BB"),
        ColorpickerItem("Red", "#F1BBBB"),
        ColorpickerItem("Green", "#C0F1BB"),
        ColorpickerItem("Blue", "#BBDAF1"),
        ColorpickerItem("Pink", "#FFD5E9"),
    )
    private lateinit var colorpickerAdapter: ColorpickerItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)
        val type = intent.getStringExtra("type")

        if (type == "text_note") {
            editTextTextMultiLine.visibility = View.VISIBLE
        } else if (type == "checkbox_list") {
            checkboxListRecyclerView.visibility = View.VISIBLE
        } else if (type == "image_note") {
            imageView.visibility = View.VISIBLE
        }

        cancelButton.setOnClickListener { finish() }

        adapter = CheckboxListAdapter(checkboxList)
        checkboxListRecyclerView.adapter = adapter

        colorpickerAdapter = ColorpickerItemAdapter(this, colorpickerList)

        noteEditOptionButton.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            showOptionMenu(popupMenu)
        }


        addCheckboxListElement()

//        itemAddButton.setOnClickListener {
//            checkboxList.add(0, CheckboxItem(itemNameInput.text.toString(), false))
//            itemNameInput.text.clear()
//            adapter.notifyDataSetChanged()
//        }
    }

    private fun showOptionMenu(popupMenu: PopupMenu) {
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.select_color_menu_item -> {
                    showColorpickerDialog()
                    true
                }
                R.id.flag_important_menu_item -> {
                    markAsImportant()
                    true
                }
                else -> false
            }
        }

        popupMenu.inflate(R.menu.note_edit_menu)
        popupMenu.show()
    }

    private fun showColorpickerDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose product")
            .setAdapter(colorpickerAdapter) { dialog, which ->
                editNoteCard.setCardBackgroundColor(Color.parseColor(colorpickerList[which].color))
            }
        val dialog = builder.create()
        dialog.show()
    }

    private fun markAsImportant() {
        importantFlagStar.visibility = if (importantFlagStar.visibility == View.VISIBLE){
            View.INVISIBLE
        } else{
            View.VISIBLE
        }
    }


    private fun addCheckboxListElement(text: String  = "", is_checked: Boolean = false) {
        checkboxList.add(CheckboxItem(text, is_checked))
        adapter.notifyDataSetChanged()
    }
}