package luis.sizzo.umbrella.model.remote

import luis.sizzo.umbrella.common.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi{

    @GET(END_POINT_W)
    fun getWeather(
        @Query(PARAM_ZIP) zip: String,
        @Query(PARAM_UNITS) units: String,
        @Query(PARAM_APPID) token: String
    ): Call<WeatherCityResponse>

    @GET(END_POINT_F)
    fun getCityWeatherDetails(
        @Query(PARAM_ZIP) zip: String,
        @Query(PARAM_UNITS) units: String,
        @Query(PARAM_APPID) token: String
    ): Call<WeatherResponse>


    companion object{
        fun initRetrofit(): WeatherApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApi::class.java)
        }
    }
}