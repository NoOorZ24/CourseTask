package lv.skudrasandbox.coursetask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {
    private val repository = NoteRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testing_button.setOnClickListener { addImageItem() }
        addCheckboxListNoteButton.setOnClickListener { createCheckboxListNote() }
        addTextNoteButton.setOnClickListener { createTextNote() }
        addImageNoteButton.setOnClickListener { createImageNote() }
    }
    private fun addImageItem() {
        testing_button.text = "aaa"
        Toast.makeText(this, getString(R.string.note_added_text), Toast.LENGTH_LONG).show()
        repository.getNote().observe(this, Observer {
            response_test.text = it.url
        })
    }

    private fun createTextNote() {
        val intent = Intent(this, CreateNoteActivity::class.java).apply {
            putExtra("type", "text_note")
        }

        startActivity(intent)
    }

    private fun createCheckboxListNote() {
        val intent = Intent(this, CreateNoteActivity::class.java).apply {
            putExtra("type", "checkbox_list")
        }

        startActivity(intent)
    }

    private fun createImageNote() {
        val intent = Intent(this, CreateNoteActivity::class.java).apply {
            putExtra("type", "image_note")
        }

        startActivity(intent)
    }
}