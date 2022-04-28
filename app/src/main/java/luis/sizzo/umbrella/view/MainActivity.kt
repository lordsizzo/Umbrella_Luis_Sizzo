package luis.sizzo.umbrella.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import luis.sizzo.umbrella.R
import luis.sizzo.umbrella.common.*
import luis.sizzo.umbrella.databinding.ActivityMainBinding
import luis.sizzo.umbrella.model.local.WeatherLocal
import luis.sizzo.umbrella.model.remote.Weather
import luis.sizzo.umbrella.view.adapter.WeatherAdapter
import luis.sizzo.umbrella.viewmodel.WeatherViewModel
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var bindingMain: ActivityMainBinding
    private val viewModel: WeatherViewModel by lazy {
        ViewModelProvider(this).get(WeatherViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)
        settings()

        initViews()
        initObservables()
        if (WeatherLocal().getSettingZipCode(this) == "") {
            Dialogs().bottomSheetDialogSettings(this, "zip_code", true) { startViewModel() }
        }
    }

    private fun initViews() {
        bindingMain.imageView.setOnClickListener {
            Intent(this, SettingsLayoutActivity::class.java).apply {
                startActivity(this)
            }
        }
        bindingMain.swipeRefreshLayoutMain.setOnRefreshListener {
            startViewModel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun initObservables() {
        viewModel.cityResponse.observe(this) {
            it.let { response ->
                try {
                    bindingMain.tvCity.text =
                        Utilities().getAddress(this, response.coord.lat, response.coord.lon)[1]
                    bindingMain.tvTemperature.text =
                        Utilities().getTemperatureFormat(this, response.main.temp)
                    bindingMain.tvDescription.text = response.weather[0].main
                    Picasso.get().load("https://openweathermap.org/img/wn/" + response.weather[0].icon + ".png").into(bindingMain.ivWeather)
                    val color = Utilities().changeColorWeather(
                        response.weather[0].id
                    )

                    bindingMain.linearLayoutWeather.setBackgroundColor(
                        getColor(
                            color
                        )
                    )

                    bindingMain.tvWeatherToday.setBackgroundColor(
                        getColor(
                            color
                        )
                    )

                    bindingMain.tvWeatherTomorrow.setBackgroundColor(
                        getColor(
                            color
                        )
                    )
                    changeBarColor(color)
                    WeatherLocal().insertCurrentColor(
                        this,
                        color
                    )
                } catch (e: Exception) {
                    bindingMain.tvCity.text = "Error"
                    bindingMain.tvTemperature.text = "Your location is not available"
                    bindingMain.tvDescription.text = ""
                    bindingMain.swipeRefreshLayoutMain.isRefreshing = false
                    Log.d("initObservables Error:", "initObservables: ${e.message}")
                }
            }
        }
        viewModel.weatherCityResponse.observe(this) {

            it.let { response ->
                try {
                    //Today's weather
                    val list = ArrayList<Weather>()
                    response.list.filter { weatherFilter ->
                        weatherFilter.dt_txt.substring(0, 10) == Utilities().getCurrentDate()
                    }.forEach { weather ->
                        list.add(weather)
                    }.let {
                        if (list.size > 0) {
                            WeatherAdapter(list, this@MainActivity).apply {
                                bindingMain.rvWeather.configRecycler(this)
                            }

                            bindingMain.llWeatherToday.visibility = View.VISIBLE
                        } else {
                            bindingMain.llWeatherToday.visibility = View.GONE
                        }
                    }

                    //Tomorrow's weather
                    val listTomorrow = ArrayList<Weather>()
                    response.list.filter { weatherFilter ->
                        weatherFilter.dt_txt.substring(0, 10) == Utilities().getTomorrowDate()
                    }.forEach { weather ->
                        listTomorrow.add(weather)
                    }.let {

                        if (listTomorrow.size > 0) {
                            WeatherAdapter(listTomorrow, this@MainActivity).apply {
                                bindingMain.rvWeatherTomorrow.configRecycler(this)
                            }
                            bindingMain.llWeatherForecast.visibility = View.VISIBLE
                        } else {
                            bindingMain.llWeatherForecast.visibility = View.GONE
                        }
                    }
                    bindingMain.swipeRefreshLayoutMain.isRefreshing = false
                } catch (e: Exception) {
                    Log.d("initObservables", "initObservables: ${e.message}")
                }
            }
        }
        startViewModel()
    }

    private fun startViewModel() {
        val save = WeatherLocal().getSettingZipCodeAndCountryCode(this)
        if (save != null) {
            viewModel.getCityWeather(save[0], save[1], save[2])
            viewModel.getWeatherResponse(save[0], save[1], save[2])
        }
    }
}