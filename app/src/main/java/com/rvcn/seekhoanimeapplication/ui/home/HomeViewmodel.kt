package com.rvcn.seekhoanimeapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvcn.seekhoanimeapplication.data.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    val repository: AnimeRepository

) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUIState> = MutableStateFlow(HomeUIState.Loading)
    val uiState : StateFlow<HomeUIState> = _uiState

    init {
        loadAnime()
    }

    fun loadAnime(){
        viewModelScope.launch {
            repository.observeAnime().collect { list->

                if(list.isEmpty())
                    _uiState.value = HomeUIState.Loading
                else{
                    val carousel = list.filter { it.rating !=null }
                        .sortedByDescending { it.rating }
                        .take(5)

                    val grid = list

                    _uiState.value = HomeUIState.Success(carousel,grid)
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