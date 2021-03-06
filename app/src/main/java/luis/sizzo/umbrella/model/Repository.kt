package luis.sizzo.umbrella.model

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import luis.sizzo.umbrella.common.WEATHER_TOKEN
import luis.sizzo.umbrella.model.remote.*

class Repository {

    suspend fun weatherCityCatched(zipCode: String, zipCountry: String, units:String): WeatherCityResponse? {
        val catchinData = GlobalScope.async {
            catchWeatherCityRetrofit(zipCode, zipCountry, units)
        }
        return catchinData.await()
    }

    suspend fun weatherResponseCatched(zipCode: String, zipCountry: String, units:String): WeatherResponse? {
        val catchinData = GlobalScope.async {
            catchWeatherResponseRetrofit(zipCode, zipCountry, units)
        }
        return catchinData.await()
    }

    private fun catchWeatherCityRetrofit(zipCode: String, zipCountry: String, units:String): WeatherCityResponse? {
        var items: WeatherCityResponse? = null
        WeatherApi.initRetrofit()
            .getWeather("$zipCountry,$zipCode", units, WEATHER_TOKEN)
            .execute()
            .body()?.let {
                items = it
            } ?: run {
                items = null
            }
        Log.d("Repository", "catchWeatherCityRetrofit: $items")
        return items
    }

    private fun catchWeatherResponseRetrofit(zipCode: String, zipCountry: String, units:String): WeatherResponse? {
        var items: WeatherResponse? = null
        WeatherApi.initRetrofit()
            .getCityWeatherDetails("$zipCountry,$zipCode", units, WEATHER_TOKEN)
            .execute()
            .body()?.let {
                items = it
            } ?: run {
            items = null
        }
        Log.d("Repository", "catchWeatherResponseRetrofit: $items")

        return items
    }
}