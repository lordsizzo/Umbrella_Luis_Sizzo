package luis.sizzo.umbrella.model.remote


data class WeatherCityResponse(
    val coord: CityCoordinates,
    val weather: List<WeatherDescription>,
    val main: CityWheater,
    val dt: Int,
)

//City
data class CityWheater(val temp: Double, val temp_min: Double, val temp_max:Double)
data class CityCoordinates(val lat: Double, val lon: Double)

data class WeatherResponse(val list: List<Weather>)

//Weather Response
data class Weather(val dt: Int, val main: WeatherMainDescription, val weather: List<WeatherDescription>, val dt_txt: String)
data class WeatherDescription(val id: Int, val main: String, val icon:String)
data class WeatherMainDescription(val temp: Double, val temp_min: Double, val temp_max:Double)