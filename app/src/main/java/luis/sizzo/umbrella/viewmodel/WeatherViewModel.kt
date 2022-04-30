package luis.sizzo.umbrella.viewmodel

import android.os.*
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import luis.sizzo.umbrella.model.Repository
import luis.sizzo.umbrella.model.remote.*

class WeatherViewModel: ViewModel() {

    private val _cityResponse = MutableLiveData<WeatherCityResponse>()
    val cityResponse: LiveData<WeatherCityResponse>
        get() = _cityResponse

    private val _weatherCityResponse = MutableLiveData<WeatherResponse>()
    val weatherCityResponse: LiveData<WeatherResponse>
        get() = _weatherCityResponse

    fun getCityWeather(zipCode: String, zipCountry: String, units:String){
        CoroutineScope(Dispatchers.IO).launch {

            var items: WeatherCityResponse?
            Repository().weatherCityCatched(zipCode, zipCountry, units).let { result ->
                items = result
            }
            Log.d("WeatherViewModel", "getCityWeather: $items")

            Handler(Looper.getMainLooper()).post {
                try {
                    _cityResponse.value = items
                } catch (e: Exception) {
                    Log.d("Error", e.toString())
                }
            }
        }
    }

    fun getWeatherResponse(zipCode: String, zipCountry: String, units:String){

        CoroutineScope(Dispatchers.IO).launch {

            var items: WeatherResponse?
            Repository().weatherResponseCatched(zipCode, zipCountry, units).let { result ->
                items = result
            }
            Log.d("getWeatherResponse", "getWeatherResponse: $items")
            Handler(Looper.getMainLooper()).post {
                try {
                    items.let {
                        _weatherCityResponse.value = items
                    }
                } catch (e: Exception) {
                    Log.d("Error", e.toString())
                }
            }
        }
    }
}