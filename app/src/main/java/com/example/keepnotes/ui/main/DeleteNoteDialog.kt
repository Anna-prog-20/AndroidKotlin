package com.example.keepnotes.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.keepnotes.R

class DeleteNoteDialog : DialogFragment() {
    companion object {
        val TAG = "${DeleteNoteDialog::class.java} TAG"
        fun createInsance() = DeleteNoteDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(context)
            .setTitle(R.string.delete_note_dialog_title)
            .setMessage(R.string.delete_note_dialog_message)
            .setPositiveButton(R.string.button_ok) { _, _ ->
                (activity as MainActivity).onDeleteNote()
            }
            .setNegativeButton(R.string.button_cancel) { _, _ -> dismiss() }
            .create()

    interface NoteListener {
        fun onDeleteNote()
    }
}