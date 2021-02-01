package com.example.keepnotes.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.keepnotes.R
import com.example.keepnotes.databinding.ActivityMainBinding
import com.example.keepnotes.model.NameActivity
import com.example.keepnotes.model.Note
import com.example.keepnotes.ui.base.BaseActivity
import com.example.keepnotes.ui.note.NoteActivity
import com.example.keepnotes.ui.splash.SplashActivity
import com.firebase.ui.auth.AuthUI


class MainActivity() : BaseActivity<List<Note>?, MainViewState>(),
    LogoutDialog.LogoutListener, DeleteNoteDialog.NoteListener {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    private val adapter: RecyclerAdapter by lazy {
        RecyclerAdapter(object : IRVOnItemClick {
            override fun onItemClicked(note: Note?) {
                openNoteScreen(note)
            }
        })
    }

    override val ui: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.fab.setOnClickListener { openNoteScreen(null) }
        setupRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemAddNote -> {
                openNoteScreen(null).let { true }
            }
            R.id.itemLogout -> {
                showLogoutDialog().let { true }
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun showLogoutDialog() {
        supportFragmentManager.findFragmentByTag(LogoutDialog.TAG) ?: LogoutDialog.createInsance()
            .show(supportFragmentManager, LogoutDialog.TAG)
    }

    private fun showDeleteNoteDialog() {
        supportFragmentManager.findFragmentByTag(DeleteNoteDialog.TAG)
            ?: DeleteNoteDialog.createInsance()
                .show(supportFragmentManager, DeleteNoteDialog.TAG)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
        MenuInflater(this).inflate(R.menu.main_menu, menu).let { true }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater(this).inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.itemChangeNote -> {
                openNoteScreen(adapter.noteSelected)
                true
            }
            R.id.itemDeleteNote -> {
                showDeleteNoteDialog().let { true }
            }
            else -> super.onContextItemSelected(item)
        }

    private fun setupRecyclerView() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        ui.listNotes.layoutManager = layoutManager
        ui.listNotes.adapter = adapter
        registerForContextMenu(ui.listNotes)
    }

    private fun openNoteScreen(note: Note?) {
        val intent = NoteActivity.getStartIntent(this, note?.id)
        startActivity(intent)
    }

    override fun renderData(data: List<Note>?) {
        adapter.notes = data ?: return
    }

    override fun onLogout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                startActivity(Intent(this, SplashActivity::class.java))
                finish()
            }
    }

    override fun onDeleteNote() {
        adapter.noteSelected?.let { noteSelected ->
            viewModel.deleteNote(noteSelected)
        }
    }
}
