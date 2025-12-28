package com.rvcn.seekhoanimeapplication.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.rvcn.seekhoanimeapplication.R
import com.rvcn.seekhoanimeapplication.databinding.ActivitySplashBinding
import com.rvcn.seekhoanimeapplication.ui.home.HomeActivity
import kotlinx.coroutines.flow.collectLatest

class SplashActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playIntroAnimation()
        observeNavigation()
    }


    private fun playIntroAnimation() {
        binding.tvAnime.apply {
            alpha = 0f
            scaleX = 0.3f
            scaleY = 0.3f

            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(800)
                .start()
        }
    }

    private fun observeNavigation(){
        lifecycleScope.launchWhenStarted {
            viewModel.navigateToHome.collectLatest { go ->
                if(go){
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                    finish()
                }
            }
        }

    }
}