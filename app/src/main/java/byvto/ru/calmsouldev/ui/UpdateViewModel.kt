package byvto.ru.calmsouldev.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import byvto.ru.calmsouldev.Resource
import byvto.ru.calmsouldev.data.remote.dto.TrackDto
import byvto.ru.calmsouldev.domain.model.Track
import byvto.ru.calmsouldev.domain.repository.CalmSoulRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val repo: CalmSoulRepo,
) : ViewModel() {

    private val _remoteList = MutableStateFlow(listOf<TrackDto>())
    val remoteList = _remoteList.asStateFlow()

    private val _localList = MutableStateFlow(listOf<Track>())
    val localList = _localList.asStateFlow()

    private val _channel = Channel<CalmSoulEvent>()
    val channel = _channel.receiveAsFlow()

    private fun showToast(message: String) {
        viewModelScope.launch {
            _channel.send(CalmSoulEvent.ShowToast(message))
        }
    }

    fun getLists() {
        viewModelScope.launch {
            _localList.value = repo.getLocalList()
            repo.getRemoteList()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _remoteList.value = result.data ?: emptyList()
                        }

                        is Resource.Error -> {
                            _remoteList.value = result.data ?: emptyList()
                            showToast(result.message ?: "Unknown error!")
                        }

                        is Resource.Loading -> {
                            _remoteList.value = result.data ?: emptyList()
                        }
                    }
                }
        }
    }
}