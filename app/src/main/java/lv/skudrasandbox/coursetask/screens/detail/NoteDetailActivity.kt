package lv.skudrasandbox.coursetask.screens.detail

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.camera.core.ImageCapture
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_create_note.*
import lv.skudrasandbox.coursetask.CheckboxListAdapter
import lv.skudrasandbox.coursetask.ColorpickerItemAdapter
import lv.skudrasandbox.coursetask.R
import lv.skudrasandbox.coursetask.api.Resource
import lv.skudrasandbox.coursetask.items.CheckboxItem
import lv.skudrasandbox.coursetask.items.ColorpickerItem
import lv.skudrasandbox.coursetask.items.NoteData
import lv.skudrasandbox.coursetask.CameraActivity
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

        getImageLayout.visibility = View.GONE;
        buttonTakeImage.visibility = View.GONE;
        buttonUploadImage.visibility = View.GONE;
        imageView.visibility = View.GONE;

        if (id.isNotEmpty()) {
            viewModel.getNote(id).observe(this, Observer { resource ->
                when (resource) {
                    is Resource.Success -> {
                        editNoteTitle.setText(resource.data.title)
                        enableInputsByType(resource.data.type)
                        editTextTextMultiLine.setText(resource.data.json)
                        editNoteCard.setCardBackgroundColor(Color.parseColor(resource.data.color))
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

        addItemButton.setOnClickListener { addCheckboxListElement() }

        addCheckboxListElement()

        buttonTakeImage.setOnClickListener { takePictureCameraX() }
        buttonUploadImage.setOnClickListener { selectImage() }
    }

    private fun takePictureCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivityForResult(intent, Companion.REQUEST_IMAGE_CAPTURE_CAMERAX)
    }

    private fun sendSms(message: String) {
        val sendIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("email@email.lv"))
            putExtra(Intent.EXTRA_SUBJECT, "email subject")
            putExtra(Intent.EXTRA_TEXT, "Some email random text ")
        }

        if (sendIntent.resolveActivity(packageManager) != null) {
            startActivity(sendIntent)
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
                getImageLayout.visibility = View.VISIBLE;
                buttonTakeImage.visibility = View.VISIBLE;
                buttonUploadImage.visibility = View.VISIBLE;
                imageView.visibility = View.VISIBLE;
            }
        }
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


    private fun addCheckboxListElement(text: String = "", is_checked: Boolean = false) {
        checkboxList.add(0, CheckboxItem(text, is_checked))
        adapter.notifyDataSetChanged()
    }

    private fun showError(message: String) {
        println(message)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    val PICK_IMAGE = 1235
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val uri = data!!.extras!!.getParcelable<Uri>("uri")
            imageView.setImageURI(uri)
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE_CAMERAX && resultCode == Activity.RESULT_OK) {
            val uri = data!!.extras!!.getParcelable<Uri>("uri")
            imageView.setImageURI(uri)
        }
    }

    fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE_CAMERAX = 2
    }
}