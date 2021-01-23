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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.keepnotes.R
import com.example.keepnotes.databinding.ActivityNoteBinding
import com.example.keepnotes.model.DATE_TIME_FORMAT
import com.example.keepnotes.model.Note
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.jaredrummler.android.colorpicker.ColorShape
import java.text.SimpleDateFormat
import java.util.*


class NoteActivity : AppCompatActivity(), ColorPickerDialogListener {

    companion object {
        private var EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

        fun getStartIntent(context: Context, note: Note?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, note)
            return intent
        }
    }

    private var note: Note? = null
    private lateinit var ui: ActivityNoteBinding
    private lateinit var viewModel: NoteViewModel
    private var colorSelected: Int = 0
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
            color = colorSelected,
            lastChanged = Date()
        )
            ?: createNewNote()

        if (note != null) viewModel.saveChanges(note!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(ui.root)

        note = intent.getParcelableExtra(EXTRA_NOTE)

        initToolBar()
        initView()

        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
    }

    fun initToolBar() {
        setSupportActionBar(ui.toolbar)
        ui.toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                triggerSaveNote()
                onBackPressed()
            }
        })
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = if (note != null) {
            SimpleDateFormat(
                DATE_TIME_FORMAT,
                Locale.getDefault()
            ).format(note!!.lastChanged)
        } else {
            getString(R.string.new_note_title)
        }
    }

    fun initView() {
        if (note != null) {
            ui.titleEt.setText(note?.topic ?: "")
            ui.textEt.setText(note?.text ?: "")
            ui.toolbar.setBackgroundColor(note!!.color)
            ui.colorSelected.setBackgroundColor(note!!.color)
            ui.titleEt.addTextChangedListener(textChangeListener)
            ui.textEt.addTextChangedListener(textChangeListener)
        }
    }

    private fun createNewNote(): Note =
        Note(
            topic = ui.titleEt.text.toString(),
            text = ui.textEt.text.toString(),
            color = colorSelected
        )

    override fun onBackPressed() {
        triggerSaveNote()
        super.onBackPressed()
    }

    override fun onColorSelected(dialogId: Int, color: Int) {
        colorSelected = color
        ui.colorSelected.setBackgroundColor(colorSelected)
        ui.toolbar.setBackgroundColor(colorSelected)
    }

    override fun onDialogDismissed(dialogId: Int) {

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun createColorPickerDialog() {
        var colorPickerDialog = ColorPickerDialog.newBuilder()
            .setColor(getColor(R.color.color_yello))
            .setDialogType(ColorPickerDialog.TYPE_PRESETS)
            .setAllowCustom(true)
            .setColorShape(ColorShape.CIRCLE)

        if (note != null) {
            colorPickerDialog.setColor(note!!.color)
        }

        colorPickerDialog.show(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onClickButton(view: View) {
        createColorPickerDialog()
    }
}
