package com.rvcn.seekhoanimeapplication.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rvcn.seekhoanimeapplication.databinding.ActivityHomeBinding
import com.rvcn.seekhoanimeapplication.ui.detailscreen.DetailActivity
import com.rvcn.seekhoanimeapplication.ui.home.adapters.AnimeGridAdapter
import com.rvcn.seekhoanimeapplication.ui.home.adapters.CarouselAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.jvm.java

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    val viewmodel: HomeViewmodel by viewModels()
    private lateinit var binding: ActivityHomeBinding

    companion object{
        private const val TAG ="HomeActivity"
    }

    private val carouselAdapter = CarouselAdapter{animeId ->

        Log.d(TAG, "clicked: id: $animeId")
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_ANIME_ID, animeId)
        startActivity(intent)
    }

    private val animeGridAdapter = AnimeGridAdapter{animeId ->

        Log.d(TAG, "clicked: id: $animeId")
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_ANIME_ID, animeId)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()
        observeState()
        setupListeners()

        viewmodel.refresh()
    }

    fun setupListeners(){
        binding.btnRetry.setOnClickListener { v->
            viewmodel.refresh()
        }
    }

    fun setupRecyclerViews(){

        binding.rvCarousel.apply {
            adapter = carouselAdapter
        }


        binding.rvAnimeGrid.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 3)
            adapter = animeGridAdapter
        }
    }

    fun observeState(){
        lifecycleScope.launchWhenStarted {
            viewmodel.uiState.collect { state ->
                when(state){
                    HomeUIState.Loading -> showLoading()
                    HomeUIState.Offline -> {
                        showOffline()
                    }
                    HomeUIState.OfflineEmpty -> showOfflineEmpty()
                    HomeUIState.BackOnline -> {
                        hideOfflineEmpty()
                        showBackOnline()
                    }
                    is HomeUIState.Error -> showError()
                    is HomeUIState.Success -> showData(state)
                }
            }
        }
    }


    private var offlineSnackbar: Snackbar? = null

    fun showOfflineSnackbar(){

        if(offlineSnackbar?.isShown == true) return

        offlineSnackbar = Snackbar.make(binding.root, "You're offline, Showing cached Anime",
            Snackbar.LENGTH_INDEFINITE) .setAction("Dismiss"){offlineSnackbar?.dismiss()}

        offlineSnackbar?.show()

    }

    fun showBackOnlineSnackbar(){

        offlineSnackbar = Snackbar.make(binding.root, "Welcome Back Online",
            Snackbar.LENGTH_INDEFINITE) .setAction("Dismiss"){offlineSnackbar?.dismiss()}

        offlineSnackbar?.show()

    }

    private fun showOfflineEmpty() {
        binding.progressBar.visibility = View.GONE
        binding.rvCarousel.visibility = View.GONE
        binding.rvAnimeGrid.visibility = View.GONE
        binding.offlineEmptyView.visibility = View.VISIBLE

        binding.btnOpenSettings.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
        }
    }

    private fun hideOfflineEmpty() {
        binding.offlineEmptyView.visibility = View.GONE
    }


    fun showOffline(){
        binding.progressBar.visibility = View.GONE
        binding.errorView.visibility = View.GONE
        binding.btnRetry.visibility = View.GONE

        binding.rvCarousel.visibility = View.VISIBLE
        binding.rvAnimeGrid.visibility = View.VISIBLE

        if (animeGridAdapter.itemCount>0) {
            showOfflineSnackbar()
        }
    }

    fun showBackOnline(){
        binding.progressBar.visibility = View.GONE
        binding.errorView.visibility = View.GONE
        binding.btnRetry.visibility = View.GONE

        binding.rvCarousel.visibility = View.VISIBLE
        binding.rvAnimeGrid.visibility = View.VISIBLE

        showBackOnlineSnackbar()
    }

    fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
    }

    fun showError(){
        binding.errorView.visibility = View.VISIBLE
    }

    fun showData(state: HomeUIState.Success){

        binding.progressBar.visibility = View.GONE
        binding.errorView.visibility = View.GONE
        binding.btnRetry.visibility = View.GONE
        binding.offlineEmptyView.visibility = View.GONE

        binding.rvCarousel.visibility = View.VISIBLE
        binding.rvAnimeGrid.visibility = View.VISIBLE

        offlineSnackbar?.dismiss()

        carouselAdapter.submitList(state.carousel)
        Log.d(TAG, "showData: grid data: ${state.grid}")
        animeGridAdapter.submitList(state.grid)
    }


}