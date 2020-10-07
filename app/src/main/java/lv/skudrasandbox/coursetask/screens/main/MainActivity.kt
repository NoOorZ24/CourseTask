package lv.skudrasandbox.coursetask.screens.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import lv.skudrasandbox.coursetask.NoteRecyclerAdapter
import lv.skudrasandbox.coursetask.screens.detail.NoteDetailActivity
import lv.skudrasandbox.coursetask.R
import lv.skudrasandbox.coursetask.api.Resource
import lv.skudrasandbox.coursetask.items.NoteData

class MainActivity : AppCompatActivity(), AdapterClickListener {

    private val notes = mutableListOf<NoteData>()

    private lateinit var adapter: NoteRecyclerAdapter

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addCheckboxListNoteButton.setOnClickListener { createCheckboxListNote() }
        addTextNoteButton.setOnClickListener { createTextNote() }
        addImageNoteButton.setOnClickListener { createImageNote() }

        adapter = NoteRecyclerAdapter(this, notes)
        mainNoteRecyclerView.adapter = adapter

        refresh()
//        mainButtonAdd.setOnClickListener { appendItem() }
        refreshLayout.setOnRefreshListener { refresh() }
    }

    private fun refresh() {
        viewModel.getNotes().observe(this, Observer {
            when (it) {
                is Resource.Loading -> refreshLayout.isRefreshing = true
                is Resource.Loaded -> refreshLayout.isRefreshing = false
                is Resource.Error -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                is Resource.Success -> {
                    notes.clear()
                    notes.addAll(it.data)
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun createTextNote() {
        val intent = Intent(this, NoteDetailActivity::class.java).apply {
            putExtra("type", "text_note")
        }

        startActivityForResult(intent, REQUEST_CODE_DETAILS)
    }

    private fun createCheckboxListNote() {
        val intent = Intent(this, NoteDetailActivity::class.java).apply {
            putExtra("type", "checkbox_list")
        }

        startActivityForResult(intent, REQUEST_CODE_DETAILS)
    }

    private fun createImageNote() {
        val intent = Intent(this, NoteDetailActivity::class.java).apply {
            putExtra("type", "image_note")
        }

        startActivityForResult(intent, REQUEST_CODE_DETAILS)
    }

    override fun itemClicked(item: NoteData) {
        val intent = Intent(this, NoteDetailActivity::class.java)
            .putExtra(EXTRA_ID, item.id)
        startActivityForResult(intent, REQUEST_CODE_DETAILS)
    }

    override fun deleteClicked(note: NoteData) {
        viewModel.removeNotes(note.id).observe(this, Observer { refresh() })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            refresh()
    }

    companion object {
        const val EXTRA_ID = "lv.skudrasandbox.coursetask.screens.main"
        const val REQUEST_CODE_DETAILS = 1234
    }
}

interface AdapterClickListener {
    fun itemClicked(item: NoteData)

    fun deleteClicked(item: NoteData)

}