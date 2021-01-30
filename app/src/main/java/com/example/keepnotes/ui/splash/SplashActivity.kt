package com.example.keepnotes.ui.splash

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.example.keepnotes.databinding.ActivitySplashBinding
import com.example.keepnotes.model.NameActivity
import com.example.keepnotes.ui.base.BaseActivity
import com.example.keepnotes.ui.main.MainActivity

private const val START_DELAY = 1000L

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {
    override val ui: ActivitySplashBinding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    override val nameActivity: NameActivity = NameActivity.splash
    override val viewModel: SplashViewModel by lazy {
        ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({ viewModel.requestUser() }, START_DELAY)
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let { startMainActivity() }
    }

    private fun startMainActivity() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }
}