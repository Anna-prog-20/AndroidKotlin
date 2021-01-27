package com.example.keepnotes.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.keepnotes.R
import com.example.keepnotes.model.Note
import com.example.keepnotes.ui.base.BaseActivity
import com.example.keepnotes.ui.note.NoteActivity
import kotlinx.android.synthetic.main.activity_main.*


abstract class MainActivity() : BaseActivity<List<Note>?, MainViewState>() {

    override val layoutRes: Int
        get() {
            return R.layout.activity_main
        }
    override val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    //lateinit var ui: ActivityMainBinding
    lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ui = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { openNoteScreen(null) }
        setupRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemAddNote -> {
                openNoteScreen(null)
                true
            }
            else -> super.onOptionsItemSelected(item)
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
        listNotes.layoutManager = layoutManager
        listNotes.adapter = adapter
    }

    private fun openNoteScreen(note: Note?) {
        val intent = NoteActivity.getStartIntent(this, note?.id)
        startActivity(intent)
    }

    override fun renderData(data: List<Note>?) {
        if (data == null) return
        adapter.notes = data
    }
}

