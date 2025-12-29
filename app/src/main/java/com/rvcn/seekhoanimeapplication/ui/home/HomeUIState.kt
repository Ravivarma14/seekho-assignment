package com.rvcn.seekhoanimeapplication.ui.home

import com.rvcn.seekhoanimeapplication.domain.model.Anime

sealed class HomeUIState {

    object Loading: HomeUIState()
    object Offline: HomeUIState()
    object BackOnline: HomeUIState()
    object OfflineEmpty : HomeUIState()

    data class Error(val message: String) : HomeUIState()
    data class Success(
        val carousel: List<Anime>,
        val grid: List<Anime>
    ) : HomeUIState()
}