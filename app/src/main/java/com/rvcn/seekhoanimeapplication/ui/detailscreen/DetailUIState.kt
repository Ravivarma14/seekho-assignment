package com.rvcn.seekhoanimeapplication.ui.detailscreen

import com.rvcn.seekhoanimeapplication.domain.model.AnimeDetail

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(
        val detail: AnimeDetail,
        val isOffline: Boolean
    ) : DetailUiState()

    data class Error(val message: String) : DetailUiState()
}


