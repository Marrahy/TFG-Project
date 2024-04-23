package com.sergimarrahyarenas.api

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModel(private val apiRepository: ApiRepository) : ViewModel() {
    val gameData = MutableLiveData<GameDataResponse>()

    fun fetchGameData(game: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = apiRepository.getGameData(game)
                    gameData.value = response
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}