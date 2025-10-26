package com.example.notekeeperapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText

class NoteEditorActivity : AppCompatActivity() {

    private lateinit var etTitle: TextInputEditText
    private lateinit var etContent: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var toolbar: MaterialToolbar
    private lateinit var dbHelper: DatabaseHelper

    private var noteId: Int = -1
    private var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)

        // Initialize views
        etTitle = findViewById(R.id.etNoteTitle)
        etContent = findViewById(R.id.etNoteContent)
        btnSave = findViewById(R.id.btnSaveNote)
        toolbar = findViewById(R.id.toolbar)

        dbHelper = DatabaseHelper(this)

        // Set up toolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        // Check if editing existing note
        noteId = intent.getIntExtra("NOTE_ID", -1)
        isEditMode = noteId != -1

        if (isEditMode) {
            toolbar.title = "Edit Note"
            loadNoteData()
        } else {
            toolbar.title = "Add Note"
        }

        // Save button click
        btnSave.setOnClickListener {
            saveNote()
        }
    }

    private fun loadNoteData() {
        val notes = dbHelper.getAllNotes()
        val note = notes.find { it.id == noteId }
        note?.let {
            etTitle.setText(it.title)
            etContent.setText(it.content)
        }
    }

    private fun saveNote() {
        val title = etTitle.text.toString().trim()
        val content = etContent.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            return
        }

        if (content.isEmpty()) {
            Toast.makeText(this, "Please enter some content", Toast.LENGTH_SHORT).show()
            return
        }

        if (isEditMode) {
            // Update existing note
            val note = Note(id = noteId, title = title, content = content)
            val rowsAffected = dbHelper.updateNote(note)
            if (rowsAffected > 0) {
                Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to update note", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Create new note
            val note = Note(title = title, content = content)
            val id = dbHelper.insertNote(note)
            if (id > 0) {
                Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show()
            }
        }
    }
}