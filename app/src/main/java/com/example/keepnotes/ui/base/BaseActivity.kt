package com.example.keepnotes.ui.base

import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.keepnotes.R
import com.example.keepnotes.databinding.ActivityMainBinding
import com.example.keepnotes.databinding.ActivityNoteBinding
import com.example.keepnotes.model.NameActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import com.google.android.material.snackbar.Snackbar.make as make1

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {

    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int
    abstract val nameActivity: NameActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)

        viewModel.getViewState().observe(this, object : Observer<S> {
            override fun onChanged(viewState: S?) {
                if (viewState == null) return
                if (viewState.data != null) renderData(viewState.data)
                if (viewState.error != null) renderError(viewState.error)
            }
        })
    }

    protected fun renderError(error: Throwable) {
        if (error.message != null) showError(error.message!!)
    }

    abstract fun renderData(data: T)

    protected fun showError(error: String) {
        val snackbar = Snackbar.make(listNotes, error, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.button_ok) { snackbar.dismiss() }
        snackbar.show()
    }
}