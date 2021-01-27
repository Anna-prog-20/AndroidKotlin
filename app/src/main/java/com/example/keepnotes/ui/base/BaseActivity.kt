package com.example.keepnotes.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.snackbar.Snackbar.make as make1

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {

    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int

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
        //snackbar.setAction(R.string.ok_bth_title, View.OnClickListener { snackbar.dismiss() })
        snackbar.show()
    }
}