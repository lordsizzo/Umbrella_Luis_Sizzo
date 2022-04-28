package luis.sizzo.umbrella.common

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import luis.sizzo.umbrella.R
import luis.sizzo.umbrella.model.local.WeatherLocal
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

class Utilities {

    fun getAddress(context: Context, lat: Double, lng: Double): List<String>{

        val values = ArrayList<String>()
        val addresses: List<Address>
        val geocoder = Geocoder(context, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            lat,
            lng,
            1
        )
        val address: String? = addresses[0].getAddressLine(0)
        values.add(address?: "")
        val city: String? = addresses[0].locality
        values.add(city?: "")
        val state: String? = addresses[0].adminArea
        values.add(state?: "")
        val country: String? = addresses[0].countryName
        values.add(country?:"")
        val postalCode: String? = addresses[0].postalCode
        values.add(postalCode?:"")
        val knownName: String? = addresses[0].featureName
        values.add(knownName?:"")

        return values
    }
    fun getTemperatureFormat(context: Context, temp: Double): String{

        var tempFormat = ""
        WeatherLocal().getSettingUnits(context)?.let { units ->
            if(units == "imperial")
                tempFormat ="${temp.roundToInt()} °F"
            else if (units == "metric")
                tempFormat ="${temp.roundToInt()} °C"
            else
                tempFormat ="${temp.roundToInt()} °K"
        }
        return tempFormat
    }

    fun getTempFormat(context: Context): String{

        var tempFormat = ""
        WeatherLocal().getSettingUnits(context)?.let { units ->
            if(units == "imperial")
                tempFormat ="°F"
            else if (units == "metric")
                tempFormat ="°C"
            else
                tempFormat ="°K"
        }
        return tempFormat
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return current.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTomorrowDate(): String {

        val current = LocalDateTime.now().plusDays(1)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return current.format(formatter)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatHour(input: String): String? {

        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val outputformat: DateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a")
        val date: Date?
        var output: String? = null
        try {
            date = df.parse(input)
            output = outputformat.format(date)
            println(output)
        } catch (pe: ParseException) {
            pe.printStackTrace()
        }
        return output
    }

    @SuppressLint("SimpleDateFormat")
    fun changeColorWeather(input: Int): Int {

        val color: Int = when(input){
            in 200..232 -> R.color.indigo
            in 300..321 -> R.color.indigo
            in 500..531 -> R.color.blue_grey
            in 600..622 -> R.color.blue_lightness
            in 700..781 -> R.color.blue_grey
            in 799..800 -> R.color.red_fruit
            in 801..804 -> R.color.deep_purple
            else -> R.color.blue_lightness
        }
        return color
    }
}