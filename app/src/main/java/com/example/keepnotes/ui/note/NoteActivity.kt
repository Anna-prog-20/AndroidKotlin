package com.example.keepnotes.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.keepnotes.R
import com.example.keepnotes.databinding.ActivityNoteBinding
import com.example.keepnotes.model.*
import com.example.keepnotes.ui.base.BaseActivity
import com.example.keepnotes.ui.main.DeleteNoteDialog
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

private const val SAVE_DELAY = 2000L

class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>(),
    DeleteNoteDialog.NoteListener {

    companion object {
        private var EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

        fun getStartIntent(context: Context, noteId: String?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, noteId)
            return intent
        }
    }

    override val ui: ActivityNoteBinding by lazy { ActivityNoteBinding.inflate(layoutInflater) }
    override val viewModel: NoteViewModel by viewModel()

    private var note: Note? = null

    private var colorSelectedPicker: Color = Color.BLUE
    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            triggerSaveNote()
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            Log.i("TAG", "onTextChanged")
        }

        override fun afterTextChanged(s: Editable?) {
            Log.i("TAG", "afterTextChanged")
        }
    }

    private fun triggerSaveNote() {
        if (ui.titleEt.text == null || ui.titleEt.text!!.length < 3) return
        Handler(Looper.getMainLooper()).postDelayed(
            {
                note = note?.copy(
                    topic = ui.titleEt.text.toString(),
                    text = ui.textEt.text.toString(),
                    color = colorSelectedPicker,
                    lastChanged = Date()
                )
                    ?: createNewNote()
                note?.let { viewModel.saveChanges(it) }
            }, SAVE_DELAY
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val noteId = intent.getStringExtra(EXTRA_NOTE)
        setOnClickView()
        initToolBar()
        setEditListener()
        noteId?.let {
            viewModel.loadNote(it)
        }
    }

    private fun setOnClickView() {
        ui.colorPicker.onColorClickListener = {
            colorSelectedPicker = it
            setColorToolBar(it)
            triggerSaveNote()
        }
        ui.titleEt.setOnClickListener {
            togglePalette()
        }
        ui.textEt.setOnClickListener {
            togglePalette()
        }
    }

    private fun initToolBar() {
        ui.toolbar.setNavigationOnClickListener {
            triggerSaveNote()
            onBackPressed()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitleToolBar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
        menuInflater.inflate(R.menu.menu_note, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed().let { true }
            R.id.itemSelectionColorNote -> togglePalette().let { true }
            R.id.itemDeleteNote -> showDeleteNoteDialog().let { true }
            else -> super.onOptionsItemSelected(item)
        }

    private fun togglePalette() {
        if (ui.colorPicker.isOpen)
            ui.colorPicker.close()
        else
            ui.colorPicker.open()
    }

    private fun showDeleteNoteDialog() {
        supportFragmentManager.findFragmentByTag(DeleteNoteDialog.TAG)
            ?: DeleteNoteDialog.createInstance()
                .show(supportFragmentManager, DeleteNoteDialog.TAG)
    }

    private fun setTitleToolBar() {
        supportActionBar?.title = note?.lastChanged?.format() ?: getString(R.string.new_note_title)
    }

    private fun setColorToolBar(color: Color) {
        ui.toolbar.setBackgroundColor(color.getColorInt(this@NoteActivity))
    }

    private fun initView() {
        note?.run {
            removeEditListener()
            if (topic != ui.titleEt.text.toString())
                ui.titleEt.setText(topic)
            if (text != ui.textEt.text.toString())
                ui.textEt.setText(text)
        }
        setEditListener()
        setColorToolBar(colorSelectedPicker)
    }

    private fun createNewNote(): Note =
        Note(
            topic = ui.titleEt.text.toString(),
            text = ui.textEt.text.toString(),
            color = colorSelectedPicker
        )

    override fun onBackPressed() {
        if (ui.colorPicker.isOpen) {
            ui.colorPicker.close()
            return
        }
        super.onBackPressed()
    }

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) finish()
        this.note = data.note
        data.note?.let { note ->
            colorSelectedPicker = note.color
        }

        initView()
        setTitleToolBar()
    }

    private fun setEditListener() {
        ui.titleEt.addTextChangedListener(textChangeListener)
        ui.textEt.addTextChangedListener(textChangeListener)
    }

    private fun removeEditListener() {
        ui.titleEt.removeTextChangedListener(textChangeListener)
        ui.textEt.removeTextChangedListener(textChangeListener)
    }

    override fun onDeleteNote() {
        note?.let {
            viewModel.deleteNote()
            super.onBackPressed()
        }
    }
}
