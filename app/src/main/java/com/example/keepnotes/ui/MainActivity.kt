package com.example.keepnotes.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.keepnotes.R
import com.example.keepnotes.databinding.ActivityMainBinding
import com.example.keepnotes.model.Note
import com.example.keepnotes.model.Repository

class MainActivity() : AppCompatActivity(),
    IRVOnItemClick {
    lateinit var ui: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    lateinit var adapter: RecyclerAdapter
    private val arrayList: ArrayList<Note> = ArrayList(1)

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        setSupportActionBar(ui.toolbar)

        initArrayList(20)
        Repository.notes = arrayList
        setupRecyclerView(arrayList)
    }

    @SuppressLint("NewApi")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.itemAddNote -> {
                val count = arrayList.size + 1
                arrayList.add(
                    Note(
                        "Заголовок №" + count,
                        "Мои новые записки!!!",
                        getColor(R.color.teal_200)
                    )
                )
                adapter.notes = arrayList
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
                        "Заголовок №" + i,
                        "Мои первые записки!!!",
                        getColor(R.color.teal_200)
                    )
                )
            else
                arrayList.add(
                    Note(
                        "Заголовок №" + i,
                        "Уже намного лучше - вывели много много заметок!!!",
                        getColor(R.color.purple_200)
                    )
                )
            i++;
        }
    }

    private fun setupRecyclerView(arrayList: ArrayList<Note>) {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = RecyclerAdapter(this)

        ui.listNotes.layoutManager = layoutManager
        ui.listNotes.adapter = adapter

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.viewState()
            .observe(this, Observer<MainViewState> { t -> t?.let { adapter.notes = it.notes } })
    }

    override fun onItemClicked(itemText: String?) {
        Toast.makeText(this, "Заметка с темой - $itemText!", Toast.LENGTH_LONG).show()
    }

}

