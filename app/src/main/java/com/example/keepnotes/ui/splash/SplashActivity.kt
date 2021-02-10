package com.example.keepnotes.ui.splash

import android.os.Handler
import android.os.Looper
import com.example.keepnotes.databinding.ActivitySplashBinding
import com.example.keepnotes.ui.base.BaseActivity
import com.example.keepnotes.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

private const val START_DELAY = 1000L

class SplashActivity : BaseActivity<Boolean>() {
    override val ui: ActivitySplashBinding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    override val viewModel: SplashViewModel by viewModel()

    override fun onResume() {
        super.onResume()
        launch {
            delay(START_DELAY)
            viewModel.requestUser()
        }
    }

    override fun renderData(data: Boolean) {
        if (data) startMainActivity()
    }

    private fun startMainActivity() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }
}