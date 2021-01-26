package com.example.keepnotes.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.keepnotes.R
import com.example.keepnotes.databinding.ActivityMainBinding
import com.example.keepnotes.model.Note
import com.example.keepnotes.model.Repository
import com.example.keepnotes.model.initArrayList
import com.example.keepnotes.ui.note.NoteActivity


class MainActivity() : AppCompatActivity() {
    lateinit var ui: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    lateinit var adapter: RecyclerAdapter
    private var arrayList: List<Note> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        setSupportActionBar(ui.toolbar)
        ui.fab.setOnClickListener { openNoteScreen(null) }
        arrayList = initArrayList(this,20)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.setNotes(arrayList)
        setupRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.itemAddNote -> {
                openNoteScreen(null)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun setupRecyclerView() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = RecyclerAdapter(object : IRVOnItemClick {
            override fun onItemClicked(note: Note?) {
                openNoteScreen(note)
            }
        })
        ui.listNotes.layoutManager = layoutManager


        viewModel.viewState()
            .observe(this, Observer<MainViewState> { state -> state?.let { adapter.notes = it.notes } })

        ui.listNotes.adapter = adapter
    }

    private fun openNoteScreen(note: Note?) {
        val intent = NoteActivity.getStartIntent(this, note)
        startActivity(intent)
    }
}

