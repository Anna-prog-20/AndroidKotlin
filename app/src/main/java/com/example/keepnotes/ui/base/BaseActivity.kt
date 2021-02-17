package com.example.keepnotes.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.example.keepnotes.R
import com.example.keepnotes.model.NoAuthException
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar

private const val RC_SIGN_IN = 458

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {

    abstract val viewModel: BaseViewModel<T, S>
    abstract val ui: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ui.root)
        ui.root.findViewById<Toolbar>(R.id.toolbar)?.let {
            setSupportActionBar(it)
        }
        viewModel.getViewState().observe(this, { viewState ->
            viewState?.apply {
                data?.let { renderData(it) }
                error?.let { renderError(it) }
            }
        })
    }

    protected open fun renderError(error: Throwable) {
        when (error) {
            is NoAuthException -> startLoginActivity()
            else -> error.message?.let { showError(it) }
        }

    }

    private fun startLoginActivity() {
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.notes)
                .setTheme(R.style.LoginStyle)
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK)
            finish()
    }

    abstract fun renderData(data: T)

    protected fun showError(error: String) {
        Snackbar.make(ui.root.findViewById(R.id.listNotes), error, Snackbar.LENGTH_INDEFINITE)
            .apply {
                setAction(R.string.button_ok) { dismiss() }
                show()
            }
    }
}