package lv.skudrasandbox.coursetask.screens.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.PopupMenu
import kotlinx.android.synthetic.main.activity_create_note.*
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import lv.skudrasandbox.coursetask.CheckboxListAdapter
import lv.skudrasandbox.coursetask.ColorpickerItemAdapter
import lv.skudrasandbox.coursetask.R
import lv.skudrasandbox.coursetask.api.Resource
import lv.skudrasandbox.coursetask.items.CheckboxItem
import lv.skudrasandbox.coursetask.items.ColorpickerItem
import lv.skudrasandbox.coursetask.items.NoteData
import lv.skudrasandbox.coursetask.screens.main.MainActivity.Companion.EXTRA_ID

class NoteDetailActivity : AppCompatActivity() {

    private val viewModel: NoteDetailViewModel by viewModels()
    private var cardBackgroundColor: String = "#F1E8BB"

    private val checkboxList = mutableListOf<CheckboxItem>()

    private val colorpickerList = mutableListOf(
        ColorpickerItem("Yellow", "#F1E8BB"),
        ColorpickerItem("Red", "#F1BBBB"),
        ColorpickerItem("Green", "#C0F1BB"),
        ColorpickerItem("Blue", "#BBDAF1"),
        ColorpickerItem("Pink", "#FFD5E9"),
    )

    private lateinit var adapter: CheckboxListAdapter
    private lateinit var colorpickerAdapter: ColorpickerItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        val id = intent.getStringExtra(EXTRA_ID).orEmpty()
        var type = intent.getStringExtra("type").orEmpty()



        if (id.isNotEmpty()) {
            viewModel.getNote(id).observe(this, Observer { resource ->
                when (resource) {
                    is Resource.Success -> {
                        editNoteTitle.setText(resource.data.title)
                        enableInputsByType(resource.data.type)
                        editTextTextMultiLine.setText(resource.data.json)
                        editNoteCard.setCardBackgroundColor(Color.parseColor(resource.data.json))
                    }
                    is Resource.Error -> showError(resource.message.orEmpty())
                }
            })
        }

        saveNoteButton.setOnClickListener {
            if (id.isNotEmpty()) {
                viewModel.updateNote(
                    id,
                    NoteData(
                        id = id,
                        title = editNoteTitle.text.toString(),
                        json = editTextTextMultiLine.text.toString(),
                        is_important = (importantFlagStar.visibility == View.VISIBLE),
                        color = cardBackgroundColor,
                        type = "text_note"
                    )
                ).observe(this, Observer {
                    when (it) {
                        is Resource.Success -> {
                            setResult(RESULT_OK)
                            finish()
                        }
                        is Resource.Error -> showError(it.message.orEmpty())
                    }
                })
            } else {
                viewModel.createNote(
                    NoteData(
                        id = "",
                        title = editNoteTitle.text.toString(),
                        json = editTextTextMultiLine.text.toString(),
                        is_important = (importantFlagStar.visibility == View.VISIBLE),
                        color = cardBackgroundColor,
                        type = "text_note"
                    )
                ).observe(this, Observer {
                    when (it) {
                        is Resource.Success -> {
                            setResult(RESULT_OK)
                            finish()
                        }
                        is Resource.Error -> showError(it.message.orEmpty())
                    }
                })
            }

        }

        enableInputsByType(type)

        cancelButton.setOnClickListener { finish() }

        adapter = CheckboxListAdapter(checkboxList)
        checkboxListRecyclerView.adapter = adapter

        colorpickerAdapter = ColorpickerItemAdapter(this, colorpickerList)

        noteEditOptionButton.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            showOptionMenu(popupMenu)
        }

//        saveNoteButton.setOnClickListener { storeNote(type) }

        addItemButton.setOnClickListener { addCheckboxListElement() }

        addCheckboxListElement()
    }

    private fun sendSms(message: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            data = Uri.parse("smsto:")  // This ensures only SMS apps respond
            putExtra("sms_body", message)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No app to handle this!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun enableInputsByType(type: String) {
        when (type) {
            "text_note" -> {
                editTextTextMultiLine.visibility = View.VISIBLE
            }
            "checkbox_list" -> {
                checkboxListRecyclerView.visibility = View.VISIBLE
                addItemButton.visibility = View.VISIBLE
            }
            "image_note" -> {
                imageView.visibility = View.VISIBLE
            }
        }
    }

//    private fun storeNote(type: String) {
//        val jsonEncodedData = checkboxList.toString();
//        NoteData(
//            "", "title sampleee", type, jsonEncodedData, editNoteCard.cardBackgroundColor.toString(), (importantFlagStar.visibility == View.VISIBLE)
//        )
//    }

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
                R.id.send_text_as_sms -> {
                    sendSms(editTextTextMultiLine.text.toString())
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
                cardBackgroundColor = colorpickerList[which].color
                editNoteCard.setCardBackgroundColor(Color.parseColor(cardBackgroundColor))
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
        checkboxList.add(0, CheckboxItem(text, is_checked))
        adapter.notifyDataSetChanged()
    }

    private fun showError(message: String) {
        println(message)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}