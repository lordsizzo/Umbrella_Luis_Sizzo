package luis.sizzo.umbrella.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import luis.sizzo.umbrella.model.Repository
import luis.sizzo.umbrella.model.remote.WeatherCityResponse
import luis.sizzo.umbrella.model.remote.WeatherResponse

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