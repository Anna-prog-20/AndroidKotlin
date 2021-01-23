package com.example.keepnotes.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.keepnotes.R
import com.example.keepnotes.databinding.ActivityMainBinding
import com.example.keepnotes.model.Note
import com.example.keepnotes.model.Repository
import com.example.keepnotes.ui.note.NoteActivity


class MainActivity() : AppCompatActivity() {
    lateinit var ui: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    lateinit var adapter: RecyclerAdapter
    private val arrayList: MutableList<Note> = mutableListOf()

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        setSupportActionBar(ui.toolbar)
        ui.fab.setOnClickListener { openNoteScreen(null) }
        initArrayList(20)
        Repository.setNotes(arrayList)
        setupRecyclerView()
    }

    @SuppressLint("NewApi")
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initArrayList(count: Int) {
        var i = 1
        while (i <= count) {
            if (i <= 5)
                arrayList.add(
                    Note(
                        topic = "Заголовок №" + i,
                        text = "Мои первые записки!!!",
                        color = getColor(R.color.color_yello)
                    )
                )
            else
                arrayList.add(
                    Note(
                        topic = "Заголовок №" + i,
                        text = "Уже намного лучше - вывели много много заметок!!!",
                        color = getColor(R.color.color_violet)
                    )
                )
            i++;
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = RecyclerAdapter(object : IRVOnItemClick {
            override fun onItemClicked(note: Note?) {
                openNoteScreen(note)
            }
        })
        ui.listNotes.layoutManager = layoutManager

        viewModel = ViewModelProvider(this).get(
            MainViewModel::
            class.java
        )
        viewModel.viewState()
            .observe(this, Observer<MainViewState>
            { t ->
                t?.let {
                    adapter.notes = it.notes
                }
            })

        ui.listNotes.adapter = adapter
    }

    private fun openNoteScreen(note: Note?) {
        val intent = NoteActivity.getStartIntent(this, note)
        startActivity(intent)
    }
}

