package com.example.weatherapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.api.APILocation
import com.example.weatherapp.api.WeatherService

class CityDialogViewModel( private val service: WeatherService ) : ViewModel() {
    var query by mutableStateOf("")

    // Guarda os resultados da API (APILocation) em vez de City, para a lista
    // de sugestões poder mostrar também o estado e o país de cada cidade.
    var suggestions by mutableStateOf<List<APILocation>>(emptyList())
        private set

    fun onQueryChanged(value: String) {
        query = value
        if (value.length < 3) {
            suggestions = emptyList()
        } else {
            service.cancelAll()
            service.search(query) { res ->
                val cities = res?.filter { it.name != null }
                cities?.let { suggestions = cities.toList() }
            }
        }
    }
}

class CityDialogViewModelFactory(private val service : WeatherService) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityDialogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CityDialogViewModel(service) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
