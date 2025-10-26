package com.example.notekeeperapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddNote: FloatingActionButton
    private lateinit var tvEmptyState: TextView
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var dbHelper: DatabaseHelper
    private val notes = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewNotes)
        fabAddNote = findViewById(R.id.fabAddNote)
        tvEmptyState = findViewById(R.id.tvEmptyState)

        dbHelper = DatabaseHelper(this)

        // Set up RecyclerView
        notesAdapter = NotesAdapter(
            notes = notes,
            onNoteClick = { note -> editNote(note) },
            onNoteLongClick = { note -> showDeleteDialog(note) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = notesAdapter

        // FAB click listener
        fabAddNote.setOnClickListener {
            val intent = Intent(this, NoteEditorActivity::class.java)
            startActivity(intent)
        }

        loadNotes()
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

    private fun loadNotes() {
        notes.clear()
        notes.addAll(dbHelper.getAllNotes())
        notesAdapter.updateNotes(notes)

        // Show/hide empty state
        if (notes.isEmpty()) {
            tvEmptyState.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            tvEmptyState.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun editNote(note: Note) {
        val intent = Intent(this, NoteEditorActivity::class.java)
        intent.putExtra("NOTE_ID", note.id)
        startActivity(intent)
    }

    private fun showDeleteDialog(note: Note) {
        AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete \"${note.title}\"?")
            .setPositiveButton("Delete") { _, _ ->
                deleteNote(note)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteNote(note: Note) {
        val rowsDeleted = dbHelper.deleteNote(note.id)
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
            loadNotes()
        } else {
            Toast.makeText(this, "Failed to delete note", Toast.LENGTH_SHORT).show()
        }
    }
}