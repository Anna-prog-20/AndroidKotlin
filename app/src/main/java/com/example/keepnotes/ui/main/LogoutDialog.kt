package com.example.keepnotes.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.keepnotes.R

class LogoutDialog : DialogFragment() {
    companion object {
        val TAG = "${LogoutDialog::class.java} TAG"
        fun createInsance() = LogoutDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(context)
            .setTitle(R.string.logout_dialog_title)
            .setMessage(R.string.logout_dialog_message)
            .setPositiveButton(R.string.button_ok) { _, _ ->
                (activity as LogoutListener).onLogout()
            }
            .setNegativeButton(R.string.button_cancel) { _, _ -> dismiss() }
            .create()

    interface LogoutListener {
        fun onLogout()
    }
}