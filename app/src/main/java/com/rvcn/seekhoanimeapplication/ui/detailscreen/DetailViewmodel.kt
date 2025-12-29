package com.rvcn.seekhoanimeapplication.ui.detailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvcn.seekhoanimeapplication.data.repository.AnimeRepository
import com.rvcn.seekhoanimeapplication.domain.model.DataSource
import com.rvcn.seekhoanimeapplication.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: AnimeRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState


    fun loadDetail(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            try {
                val result = repository.getAnimeDetail(id)

                _uiState.value = DetailUiState.Success(
                    detail = result.detail,
                    isOffline = result.source == DataSource.CACHE
                )
            } catch (e: Exception) {
                val isConnected = networkMonitor.isConnected.first()

                _uiState.value =
                    if (!isConnected) {
                        DetailUiState.Error(
                            "You're offline and no cached data is available"
                        )
                    } else {
                        DetailUiState.Error(
                            "Unable to load details"
                        )
                    }
            }
        }
    }

}

