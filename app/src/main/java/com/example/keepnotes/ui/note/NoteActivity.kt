package com.example.keepnotes.ui.note

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.keepnotes.R
import com.example.keepnotes.databinding.ActivityNoteBinding
import com.example.keepnotes.model.NameActivity
import com.example.keepnotes.model.Note
import com.example.keepnotes.model.format
import com.example.keepnotes.ui.base.BaseActivity
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.jaredrummler.android.colorpicker.ColorShape
import java.util.*


class NoteActivity : BaseActivity<Note?, NoteViewState>(),
    ColorPickerDialogListener {

    companion object {
        private var EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

        fun getStartIntent(context: Context, noteId: String?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, noteId)
            return intent
        }
    }

    override val ui: ActivityNoteBinding by lazy { ActivityNoteBinding.inflate(layoutInflater) }
    override val nameActivity: NameActivity = NameActivity.note
    override val viewModel: NoteViewModel by lazy { ViewModelProvider(this).get(NoteViewModel::class.java) }

    private var note: Note? = null
    private var colorSelectedInt: Int = 0
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
        note = note?.copy(
            topic = ui.titleEt.text.toString(),
            text = ui.textEt.text.toString(),
            color = colorSelectedInt,
            lastChanged = Date()
        )
            ?: createNewNote()

        if (note != null) viewModel.saveChanges(note!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val noteId = intent.getStringExtra(EXTRA_NOTE)
        initToolBar()
        noteId?.let { viewModel.loadNote(it) }
    }

    private fun initToolBar() {
        ui.toolbar.setNavigationOnClickListener {
            triggerSaveNote()
            onBackPressed()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitleToolBar()

    }

    private fun setTitleToolBar() {
        supportActionBar?.title = note?.lastChanged?.format() ?: getString(R.string.new_note_title)
    }

    private fun initView() {
        note?.run {
            ui.titleEt.setText(topic)
            ui.textEt.setText(text)
            ui.toolbar.setBackgroundColor(color)
            ui.colorSelected.setBackgroundColor(color)
        }
        ui.titleEt.addTextChangedListener(textChangeListener)
        ui.textEt.addTextChangedListener(textChangeListener)
    }

    private fun createNewNote(): Note =
        Note(
            topic = ui.titleEt.text.toString(),
            text = ui.textEt.text.toString(),
            color = colorSelectedInt
        )

    override fun onBackPressed() {
        triggerSaveNote()
        super.onBackPressed()
    }

    override fun onColorSelected(dialogId: Int, color: Int) {
        colorSelectedInt = color
        ui.colorSelected.setBackgroundColor(colorSelectedInt)
        ui.toolbar.setBackgroundColor(colorSelectedInt)
    }

    override fun onDialogDismissed(dialogId: Int) {

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun createColorPickerDialog() {
        val colorPickerDialog = ColorPickerDialog.newBuilder()
            .setColor(getColor(R.color.color_note))
            .setDialogType(ColorPickerDialog.TYPE_PRESETS)
            .setAllowCustom(true)
            .setColorShape(ColorShape.CIRCLE)

        note?.let { colorPickerDialog.setColor(it.color) }
        colorPickerDialog.show(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onClickButton(view: View) {
        createColorPickerDialog()
    }

    override fun renderData(data: Note?) {
        this.note = data
        colorSelectedInt = note?.color ?: 0
        initView()
        setTitleToolBar()
    }
}
