package com.rvcn.seekhoanimeapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvcn.seekhoanimeapplication.data.repository.AnimeRepository
import com.rvcn.seekhoanimeapplication.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val repository: AnimeRepository,
    private val networkMonitor: NetworkMonitor

) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUIState> = MutableStateFlow(HomeUIState.Loading)
    val uiState : StateFlow<HomeUIState> = _uiState

    init {
        loadAnime()

        viewModelScope.launch {
            networkMonitor.isConnected.collect { connected ->
                if (connected) {
                    _uiState.value = HomeUIState.BackOnline
                    refresh()
                }
            }
        }
    }

    fun loadAnime() {
        viewModelScope.launch {
            repository.observeAnime().collect { list ->


                val isConnected = networkMonitor.isConnected.first()

                if (list.isEmpty()) {

                    if (!isConnected) {
                        _uiState.value = HomeUIState.OfflineEmpty
                    } else {
                        _uiState.value = HomeUIState.Loading
                    }

                } else {
                    val carousel = list
                        .filter { it.rating != null }
                        .sortedByDescending { it.rating }
                        .take(5)

                    _uiState.value =
                        HomeUIState.Success(carousel, list)
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                repository.refreshAnime()
            } catch (e: Exception) {
                _uiState.value = HomeUIState.Offline
            }
        }
    }
}