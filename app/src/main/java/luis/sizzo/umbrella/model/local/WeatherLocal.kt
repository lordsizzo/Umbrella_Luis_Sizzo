package luis.sizzo.umbrella.model.local

import android.content.Context
import android.content.SharedPreferences

class WeatherLocal {
    private lateinit var shared : SharedPreferences
    private val ZIP_KEY = "zip_key"
    private val ZIP_COUNTRY_KEY = "zip_country_code_key"
    private val UNITS_KEY = "units_key"
    private val SETTINGS_NAME = "settings"
    private val COLOR_HOURS = "settings"

    fun insertSettingsZip(context: Context, valueZip: String, valueCountryCode: String){
        shared = context.getSharedPreferences(SETTINGS_NAME , Context.MODE_PRIVATE)
        val edit = shared.edit()
        edit.putString(ZIP_KEY , valueZip)
        edit.putString(ZIP_COUNTRY_KEY , valueCountryCode)
        edit.apply()
    }

    fun insertSettingsUnits(context: Context, valueUnits: String){
        shared = context.getSharedPreferences(SETTINGS_NAME , Context.MODE_PRIVATE)
        val edit = shared.edit()
        edit.putString(UNITS_KEY , valueUnits)
        edit.apply()
    }

    fun insertCurrentColor(context: Context, hexa: Int){
        shared = context.getSharedPreferences(SETTINGS_NAME , Context.MODE_PRIVATE)
        val edit = shared.edit()
        edit.putInt(COLOR_HOURS , hexa)
        edit.apply()
    }
    fun getSettingZipCode(context: Context): String? {
        shared = context.getSharedPreferences(SETTINGS_NAME , Context.MODE_PRIVATE)
        return shared.getString(ZIP_KEY , "")
    }
    fun getSettingZipCountryCode(context: Context): String? {
        shared = context.getSharedPreferences(SETTINGS_NAME , Context.MODE_PRIVATE)
        return shared.getString(ZIP_COUNTRY_KEY , "")
    }
    fun getSettingZipCodeAndCountryCode(context: Context): List<String>? {
        shared = context.getSharedPreferences(SETTINGS_NAME , Context.MODE_PRIVATE)
        var items = ArrayList<String>()
        items.add(shared.getString(ZIP_KEY, "")?: "")
        items.add(shared.getString(ZIP_COUNTRY_KEY, "")?: "")
        items.add(shared.getString(UNITS_KEY, "")?: "")
        return items
    }

    fun getSettingUnits(context: Context): String? {
        shared = context.getSharedPreferences(SETTINGS_NAME , Context.MODE_PRIVATE)
        return shared.getString(UNITS_KEY, "")
    }

    fun getSettingColor(context: Context): Int? {
        shared = context.getSharedPreferences(SETTINGS_NAME , Context.MODE_PRIVATE)
        return shared.getInt(COLOR_HOURS, 0)
    }
}